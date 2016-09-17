package se.sugarest.jane.coffeeshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static se.sugarest.jane.coffeeshop.R.string.total;

/**
 * * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) findViewById(R.id.edit_name);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate    is whether or not the user wants chocolate
     * @return total price
     */

    public int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        // Price of 1 cup of coffee
        int pricePerCoffe = 5;

        // Add $1 if the user wants whipped cream
        if (hasWhippedCream) {
            pricePerCoffe += 1;
        }

        // Add $2 if the user wants chocolate
        if (hasChocolate) {
            pricePerCoffe += 2;
        }

        // Calculate the total order price by multiplying by quantity
        return pricePerCoffe * quantity;
    }

    /**
     * Create summary message.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate    is whether or not the user wants chocolate
     * @return summary message
     */
    private String createOrderSummary(String name, boolean hasWhippedCream, boolean hasChocolate, int totalPrice) {

        String needWhippedCream = "";

        if (hasWhippedCream) {
            needWhippedCream = getString(R.string.add_true);
        } else {
            needWhippedCream = getString(R.string.add_false);
        }

        String needChocolate = "";

        if (hasChocolate) {
            needChocolate = getString(R.string.add_true);
        } else {
            needChocolate = getString(R.string.add_false);
        }

        String summary = getString(R.string.order_summary_name, name);
        summary += "\n" + getString(R.string.add_whipped_cream, needWhippedCream);
        summary += "\n" + getString(R.string.add_chocolate, needChocolate);
        summary += "\n" + getString(R.string.order_summary_quantity, quantity);
        summary += "\n" + getString(total, totalPrice);
        summary += "\n" + getString(R.string.thank_you);
        return summary;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        String name = getUserName();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox ChocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = ChocolateCheckBox.isChecked();

        int totalPrice = calculatePrice(hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(name, hasWhippedCream, hasChocolate, totalPrice));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        View anotherview = this.getCurrentFocus();
        if (anotherview != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(anotherview.getWindowToken(), 0);
        }
    }

    /**
     * This method is called to get user name.
     */

    public String getUserName() {
        // Find user name
        EditText editText = (EditText) findViewById(R.id.edit_name);
        return editText.getText().toString();
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, getString(R.string.coffee_limit_up), Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */

    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, getString(R.string.coffee_limit_down), Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;}
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */

    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


}
