package tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Created by user on 31.05.2017.
 */
public class GradientTag extends BodyTagSupport {
    String color;
    int colorType = -1;
    
    @Override
    public int doStartTag() throws JspException {
        if(color.toUpperCase().equals("RED")) colorType = 0;
        else if(color.toUpperCase().equals("GREEN")) colorType = 1;
        else if(color.toUpperCase().equals("BLUE")) colorType = 2;
        if(colorType == -1) throw new JspException("Incorrect color");
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doAfterBody() throws JspException {
        StringBuilder result = new StringBuilder();
        String content = getBodyContent().getString();        
        int[] colors = new int[3];
        
        int delta = 1;
        if(content.length() < 255) delta = 255 / content.length();
        colors[colorType] = 90;
        
        for(int i=0;i<content.length();i++) {
            result.append("<span style='color:rgb(" + colors[0] + "," + colors[1] + "," + colors[2] + ")'>" + 
                    content.charAt(i) + "</span>");
            
            colors[colorType] += delta;
            if(colors[colorType] > 255) colors[colorType] = 0;
        }

        try {
            getBodyContent().getEnclosingWriter().println(result.toString());
        } catch (IOException e) {
            throw new JspException("Output exception");
        }
        return SKIP_BODY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
