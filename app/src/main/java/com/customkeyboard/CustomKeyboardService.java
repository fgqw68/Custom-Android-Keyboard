package com.customkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ViewFlipper;

public class CustomKeyboardService extends InputMethodService {

    private ViewFlipper viewFlipper;
    private Button tabFunction;
    private Button tabAlpha;
    private Button tabNumeric;

    private static final int DISPLAYED_CHILD_FUNCTION = 0;
    private static final int DISPLAYED_CHILD_ALPHA = 1;
    private static final int DISPLAYED_CHILD_NUMERIC = 2;

    @Override
    public View onCreateInputView() {
        // Inflate the main keyboard layout
        LayoutInflater inflater = getLayoutInflater();
        View keyboardView = inflater.inflate(R.layout.keyboard_main, null);

        // Initialize ViewFlipper
        viewFlipper = keyboardView.findViewById(R.id.keyboard_flipper);

        // Initialize tab buttons
        tabFunction = keyboardView.findViewById(R.id.tab_function);
        tabAlpha = keyboardView.findViewById(R.id.tab_alpha);
        tabNumeric = keyboardView.findViewById(R.id.tab_numeric);

        // Set up tab button listeners
        setupTabListeners();

        // Set up centralized key listener for all buttons
        setupKeyListeners(keyboardView);

        return keyboardView;
    }

    private void setupTabListeners() {
        tabFunction.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(DISPLAYED_CHILD_FUNCTION);
            updateTabStyles(DISPLAYED_CHILD_FUNCTION);
        });

        tabAlpha.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(DISPLAYED_CHILD_ALPHA);
            updateTabStyles(DISPLAYED_CHILD_ALPHA);
        });

        tabNumeric.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(DISPLAYED_CHILD_NUMERIC);
            updateTabStyles(DISPLAYED_CHILD_NUMERIC);
        });

        // Set initial tab (Alpha)
        viewFlipper.setDisplayedChild(DISPLAYED_CHILD_ALPHA);
        updateTabStyles(DISPLAYED_CHILD_ALPHA);
    }

    private void updateTabStyles(int activeTabIndex) {
        // Reset all tabs to normal background
        tabFunction.setBackgroundColor(0xFF000000); // Black
        tabAlpha.setBackgroundColor(0xFF000000);    // Black
        tabNumeric.setBackgroundColor(0xFF000000);  // Black

        // Highlight active tab with dark gray
        Button activeTab;
        switch (activeTabIndex) {
            case DISPLAYED_CHILD_FUNCTION:
                activeTab = tabFunction;
                break;
            case DISPLAYED_CHILD_ALPHA:
                activeTab = tabAlpha;
                break;
            case DISPLAYED_CHILD_NUMERIC:
                activeTab = tabNumeric;
                break;
            default:
                activeTab = tabAlpha;
        }
        activeTab.setBackgroundColor(0xFF333333); // Dark gray for active tab
    }

    private void setupKeyListeners(View keyboardView) {
        // Get all buttons in the layout
        java.util.ArrayList<View> buttons = new java.util.ArrayList<>();
        findAllButtons(keyboardView, buttons);

        // Set up centralized listener for all buttons
        View.OnClickListener keyListener = v -> {
            String text = ((Button) v).getText().toString();
            String tag = (String) v.getTag();

            if (tag != null && tag.startsWith("keycode:")) {
                // Handle system key event (F-keys, Backspace, Enter)
                int keyCode = Integer.parseInt(tag.substring(8));
                handleSystemKey(keyCode);
            } else if (tag != null && tag.startsWith("tab_")) {
                // Tab buttons are handled separately, ignore here
                return;
            } else {
                // Handle text input (characters, numbers, symbols)
                if (!text.isEmpty()) {
                    handleChar(text);
                }
            }
        };

        // Apply listener to all buttons
        for (View button : buttons) {
            if (button instanceof Button) {
                ((Button) button).setOnClickListener(keyListener);
            }
        }
    }

    private void findAllButtons(View view, java.util.ArrayList<View> buttons) {
        if (view instanceof Button) {
            buttons.add(view);
        } else if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findAllButtons(group.getChildAt(i), buttons);
            }
        }
    }

    /**
     * Handle system key events (F-keys, Backspace, Enter)
     * These use KeyEvent.ACTION_DOWN and ACTION_UP to simulate hardware key presses
     */
    private void handleSystemKey(int keyCode) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            // Send key down event
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
            // Send key up event
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode));
        }
    }

    /**
     * Handle text input for characters, numbers, and symbols
     * Uses InputConnection.commitText to insert text into the current input field
     */
    private void handleChar(String character) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.commitText(character, 1);
        }
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Called when the input view is being shown
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        // Called when the input view is being hidden
    }
}