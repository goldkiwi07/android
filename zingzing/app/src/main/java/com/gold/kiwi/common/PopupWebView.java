package com.gold.kiwi.common;

import android.content.Context;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.webkit.WebView;

/**
 * Created by KimGunWoo on 2017-12-16.
 */
public class PopupWebView extends WebView
{
    private final String TAG = getClass().getSimpleName();

    public PopupWebView(Context context)
    {
        super(context);
    }

    public PopupWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PopupWebView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onCheckIsTextEditor()
    {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //Log.d(TAG, "onTouch : " + event.getAction());

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            {
                //Log.d(TAG, "hasFocus : "+ hasFocus());

                if(!hasFocus())
                    requestFocus();

                break;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        boolean first = super.dispatchKeyEvent(event);

        if(event.getAction() == KeyEvent.ACTION_DOWN)
            Log.d(TAG, "ACTION_DOWN KeyCode : "+ event.getKeyCode() +", Action : "+ event.getAction());

        /*
        if(event.getAction() == KeyEvent.ACTION_UP)
            Log.d(TAG, "ACTION_UP KeyCode : "+ event.getKeyCode() +", Action : "+ event.getAction());
        */

        return first;
    }

    /*
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        super.onCreateInputConnection(outAttrs);

        Log.d(TAG, "FieldName : "+ outAttrs.fieldName);
        Log.d(TAG, "PrivateImeOptions : "+ outAttrs.privateImeOptions);
        Log.d(TAG, "actionId : "+ outAttrs.actionId);
        Log.d(TAG, "actionLabel : "+ outAttrs.actionLabel);
        Log.d(TAG, "label : "+ outAttrs.label);

        Log.d(TAG, "outAttrs : "+ outAttrs.toString());
        Log.d(TAG, "fieldId : "+ outAttrs.fieldId);
        Log.d(TAG, "hintText : "+ outAttrs.hintText);
        Log.d(TAG, "imeOptions : "+ outAttrs.imeOptions);
        Log.d(TAG, "inputType : "+ outAttrs.inputType);

        //outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        //outAttrs.inputType = EditorInfo.TYPE_CLASS_TEXT;
        //outAttrs.inputType = EditorInfo.TYPE_CLASS_NUMBER;

        //return new BaseInputConnection(this, false);
        return new EditableInputConnection(this);
        //return super.onCreateInputConnection(outAttrs);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Log.d(TAG, "onKeyDown : " + event.getAction());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        Log.d(TAG, "onKeyUp : " + event.getAction());
        return super.onKeyUp(keyCode, event);
    }
    */

    public class EditableInputConnection extends BaseInputConnection
    {
        private final View view;
        private SpannableStringBuilder editable;

        public EditableInputConnection(View view)
        {
            super(view, false);
            this.view = view;
        }

/*
        @Override
        public Editable getEditable()
        {
            if(editable == null)
                editable = (SpannableStringBuilder)Editable.Factory.getInstance().newEditable("Placeholder");

            return editable;
        }
*/

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition)
        {
            Log.d(TAG, "commitText : " + text);

            //invalidate();
            //editable.append(text);
            //((EditText)view).setText(text);
            //Log.d(TAG, "commitText "+ editable.toString());


            //return true;
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean setComposingText(CharSequence text, int newCursorPosition)
        {
            Log.d(TAG, "setComposingText : " + text);
            KeyEvent key = new KeyEvent(SystemClock.uptimeMillis(), text.toString(), 0, KeyEvent.FLAG_EDITOR_ACTION);

            //sendKeyEvent(key);

            String test = text.toString();

            //sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, Integer.toHexString((int)text.charAt(test.length()-1))));

            Log.d(TAG, "setComposingText : " + text.toString());

            //sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.keyCodeFromString(text.toString())));

            //return true;
            return super.setComposingText(text, newCursorPosition);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event)
        {
            Log.d(TAG, "sendKeyEvent : "+ event.getCharacters() +", "+ event.getKeyCode()+", "+ KeyEvent.keyCodeToString(event.getKeyCode()));
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean finishComposingText()
        {
            Log.d(TAG, "finishComposingText");
            return super.finishComposingText();
        }
    }
}