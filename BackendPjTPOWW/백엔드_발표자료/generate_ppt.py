
import os
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.enum.text import PP_ALIGN
from pptx.dml.color import RGBColor

# Configuration
MD_FILE = "backend_presentation.md"
PPTX_FILE = "TPOWWPj_Backend_Presentation.pptx"

# Style Constants
NAVY_BLUE = RGBColor(0, 0, 128)
LIGHT_GRAY = RGBColor(240, 240, 240)
BLACK = RGBColor(0, 0, 0)
WHITE = RGBColor(255, 255, 255)

class PresentationGenerator:
    def __init__(self, md_path, pptx_path):
        self.md_path = md_path
        self.pptx_path = pptx_path
        self.prs = Presentation()
        self.current_slide = None
        self.content_lines = []
        self.parsing_code = False
        self.code_lines = []

    def run(self):
        with open(self.md_path, "r", encoding="utf-8") as f:
            lines = f.readlines()

        for line in lines:
            line = line.rstrip()
            
            # Handle Code Blocks
            if line.strip().startswith("```"):
                if self.parsing_code:
                    # End of code block
                    self.parsing_code = False
                    self.add_code_block(self.code_lines)
                    self.code_lines = []
                else:
                    # Start of code block
                    self.parsing_code = True
                    # If start of code block has language identifier, ignore it for now
                continue
            
            if self.parsing_code:
                self.code_lines.append(line)
                continue

            # Skip separator lines
            if line.strip() == "---":
                continue

            # Process Headers
            if line.startswith("# "):
                # Title Slide (H1)
                title_text = line[2:].strip()
                self.add_title_slide(title_text)
            elif line.startswith("## "):
                # New Content Slide (H2)
                title_text = line[3:].strip()
                self.add_content_slide(title_text)
            elif line.startswith("### "):
                # Subheader (H3) - Treated as a strong boolean point or sub-slide title depending on context
                # For simplicity, we'll add it as a bold line in the current slide
                self.add_text_line(line[4:].strip(), bold=True, level=0)
            elif line.startswith("#### "):
                 self.add_text_line(line[5:].strip(), bold=True, level=1)
            
            # Process Lists
            elif line.strip().startswith("- ") or line.strip().startswith("* "):
                indent = 0
                if line.startswith("    ") or line.startswith("\t"):
                    indent = 1
                text = line.strip()[2:].strip()
                self.add_bullet_point(text, level=indent)
            elif line.strip().replace(".","").isdigit(): 
                # Ordered list detection might be tricky with just "1.", but let's try generic detection
                # Very simple heuristic: if it starts with digit + dot
                parts = line.strip().split(".", 1)
                if len(parts) > 1 and parts[0].isdigit():
                     self.add_bullet_point(line.strip(), level=0)
                else:
                    if line.strip(): self.add_text_line(line.strip())
            
            # Process Other Text
            elif line.strip():
                # Mermaid Diagram Placeholder
                if "mermaid" in line.lower() or "flowchart" in line.lower() or "sequenceDiagram" in line.lower() or "erDiagram" in line.lower():
                     self.add_placeholder_box("[여기에 아키텍처/ERD 다이어그램 이미지 삽입 필요]")
                else:
                    self.add_text_line(line.strip())

        self.prs.save(self.pptx_path)
        print(f"Presentation saved to {self.pptx_path}")

    def add_title_slide(self, title):
        slide_layout = self.prs.slide_layouts[0] # Title Slide
        slide = self.prs.slides.add_slide(slide_layout)
        title_shape = slide.shapes.title
        subtitle_shape = slide.placeholders[1]
        
        title_shape.text = title
        title_shape.text_frame.paragraphs[0].font.color.rgb = NAVY_BLUE
        title_shape.text_frame.paragraphs[0].font.bold = True
        
        subtitle_shape.text = "Backend Final Project" 
        self.current_slide = slide
        self.textbox_top = Inches(2.0) # Reset content position

    def add_content_slide(self, title):
        slide_layout = self.prs.slide_layouts[1] # Title and Content
        slide = self.prs.slides.add_slide(slide_layout)
        
        # Style Title
        title_shape = slide.shapes.title
        title_shape.text = title
        title_shape.text_frame.paragraphs[0].font.color.rgb = NAVY_BLUE
        title_shape.text_frame.paragraphs[0].font.bold = True
        title_shape.text_frame.paragraphs[0].font.size = Pt(32)

        self.current_slide = slide
        # self.body_shape = slide.placeholders[1] 
        # Instead of using the default placeholder which forces bullets, let's use a text box for more control or just use the placeholder carefully.
        # Let's use the standard placeholder for bullets to keep it native.
        self.body_shape = slide.placeholders[1]
        self.body_shape.text_frame.clear() # Clear default "Click to add text"

    def add_text_line(self, text, bold=False, level=0):
        if not self.current_slide: return
        # If we are on a title slide, maybe add to subtitle? No, assume content slide structure mostly.
        if not hasattr(self, 'body_shape'): return

        p = self.body_shape.text_frame.add_paragraph()
        p.text = text
        p.level = level
        if bold:
            p.font.bold = True

    def add_bullet_point(self, text, level=0):
        if not self.current_slide: return
        if not hasattr(self, 'body_shape'): return

        p = self.body_shape.text_frame.add_paragraph()
        p.text = text
        p.level = level

    def add_code_block(self, lines):
        if not self.current_slide: return
        
        # Create a text box for code
        left = Inches(0.5)
        top = Inches(2.5) # Approximate position, might overlap if not careful
        width = Inches(9.0)
        height = Inches(4.0)
        
        # Adjust top based on existing content if possible, but simple stacking is hard without tracking height.
        # For this script, we'll just place it at a fixed position or append to body if it's small?
        # Requirement says "box with light gray background".
        
        # Let's create a new shape for code to distinguish it.
        # To avoid valid overlap, we might want to create a new slide if the previous one is full, but that's complex.
        # We will simply add it as a separate text box on top of the current layout.
        
        txBox = self.current_slide.shapes.add_textbox(left, Inches(3.5), width, height)
        tf = txBox.text_frame
        tf.word_wrap = True
        
        # Background color
        fill = txBox.fill
        fill.solid()
        fill.fore_color.rgb = LIGHT_GRAY

        text = "\n".join(lines)
        if len(text) > 1000: # Truncate if too long
            text = text[:1000] + "\n... (Code Truncated)"

        p = tf.paragraphs[0]
        p.text = text
        p.font.name = "Consolas"
        p.font.size = Pt(10)
        p.font.color.rgb = BLACK

    def add_placeholder_box(self, text):
        if not self.current_slide: return
        
        left = Inches(2.0)
        top = Inches(3.0)
        width = Inches(6.0)
        height = Inches(2.0)
        
        shape = self.current_slide.shapes.add_shape(
            1, # msoShapeRectangle
            left, top, width, height
        )
        shape.fill.solid()
        shape.fill.fore_color.rgb = RGBColor(200, 200, 200) # Darker gray for placeholder
        
        shape.text_frame.text = text
        shape.text_frame.paragraphs[0].alignment = PP_ALIGN.CENTER

if __name__ == "__main__":
    if not os.path.exists(MD_FILE):
        print(f"Error: {MD_FILE} not found.")
    else:
        generator = PresentationGenerator(MD_FILE, PPTX_FILE)
        generator.run()
