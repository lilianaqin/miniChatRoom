package Client;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;



public class FontAttrib implements Serializable 
{
	private static final long serialVersionUID = -997341376862932444L;
	public static final int GENERAL = 0; // ����

	public static final int BOLD = 1; // ����

	public static final int ITALIC = 2; // б��

	public static final int BOLD_ITALIC = 3; // ��б��
	
	public SimpleAttributeSet attrSet = null; // ���Լ�

	public String text = null, name = null; // Ҫ������ı�����������

	public int style = 0, size = 0; // ��ʽ���ֺ�

	public Color color = null, backColor = null; // ������ɫ�ͱ�����ɫ

		  /**
		   * һ���յĹ��죨�ɵ�������ʹ�ã�
		   */
	public FontAttrib() {
	}

		  /**
		   * �������Լ�
		   * 
		   * @return
		   */
	public SimpleAttributeSet getAttrSet() {
	   attrSet = new SimpleAttributeSet();
	   if (name != null)
		   StyleConstants.setFontFamily(attrSet, name);
	   if (style == FontAttrib.GENERAL) {
		   StyleConstants.setBold(attrSet, false);
		   StyleConstants.setItalic(attrSet, false);
	   } else if (style == FontAttrib.BOLD) {
		   StyleConstants.setBold(attrSet, true);
		   StyleConstants.setItalic(attrSet, false);
	   } else if (style == FontAttrib.ITALIC) {
		   StyleConstants.setBold(attrSet, false);
		   StyleConstants.setItalic(attrSet, true);
	   } else if (style == FontAttrib.BOLD_ITALIC) {
		   StyleConstants.setBold(attrSet, true);
		   StyleConstants.setItalic(attrSet, true);
	   }
	   		StyleConstants.setFontSize(attrSet, size);
	   		if (color != null)
	   			StyleConstants.setForeground(attrSet, color);
	   		if (backColor != null)
	   			StyleConstants.setBackground(attrSet, backColor);
	   		return attrSet;
		}

		  /**
		   * �������Լ�
		   * 
		   * @param attrSet
		   */
		  public void setAttrSet(SimpleAttributeSet attrSet) {
		   this.attrSet = attrSet;
		  }

		  /* �����ע�;Ͳ�д�ˣ�һ�������� */

		  public String getText() {
		   return text;
		  }

		  public void setText(String text) {
		   this.text = text;
		  }

		  public Color getColor() {
		   return color;
		  }

		  public void setColor(Color color) {
		   this.color = color;
		  }

		  public Color getBackColor() {
		   return backColor;
		  }

		  public void setBackColor(Color backColor) {
		   this.backColor = backColor;
		  }

		  public String getName() {
		   return name;
		  }

		  public void setName(String name) {
		   this.name = name;
		  }

		  public int getSize() {
		   return size;
		  }

		  public void setSize(int size) {
		   this.size = size;
		  }

		  public int getStyle() {
		   return style;
		  }

		  public void setStyle(int style) {
		   this.style = style;
		  }
		 }