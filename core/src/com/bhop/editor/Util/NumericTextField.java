package com.bhop.editor.Util;

import java.awt.TextField ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;

import javax.swing.JTextField;

/**
 * Util class for handling numeric only textFields
 */
public class NumericTextField extends JTextField
{
    public NumericTextField (String _initialStr, int _col)
    {
        super (_initialStr, _col) ;

        this.addKeyListener(new KeyAdapter()
        {
            public void keyTyped (KeyEvent e)
            {
                char c = e.getKeyChar() ;

                if (!(  (c==KeyEvent.VK_BACK_SPACE)     || (c==KeyEvent.VK_DELETE)
                    ||  (c== KeyEvent.VK_ENTER)         || (c == KeyEvent.VK_TAB)
                    ||  (Character.isDigit(c))          || c == '.' || c== ',' || "-".contains(""+c))   )
                {
                    e.consume() ;
                }
                /**
                 * for float parsing
                 */
                if(c == ','){
                    e.setKeyChar('.');
                }
                /**
                 * The contents seem to update after this function finishes executing
                 */
            //System.out.print("Input in a numeric field:"+getContent()+"\n");
            }
        });
    }

    public float getContent(){
        if(this.getText().equals(""))return 0;
        try{
            return Float.parseFloat(this.getText());
        }catch (Exception e){
            System.out.print("Couldn't parse numeric text field:"+this.getText()+"\n");
            return 0;
        }
    }

    public NumericTextField (int _col)
    {
        this ("", _col) ;
    }
}