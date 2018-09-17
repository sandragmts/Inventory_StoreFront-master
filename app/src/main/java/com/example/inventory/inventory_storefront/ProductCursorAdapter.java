package com.example.inventory.inventory_storefront;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inventory.inventory_storefront.R;
import com.example.inventory.inventory_storefront.data.ProductContract;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView productNameTextView = view.findViewById(R.id.product_Name);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);
        ImageView sellProductImageView = view.findViewById(R.id.tag);

        // Find the columns of product attributes that we're interested in
        int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);

        // Read the product attributes from the Cursor for the current product
        final int productID = cursor.getInt(idColumnIndex);
        String productName = cursor.getString(productNameColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        double price = cursor.getDouble(priceColumnIndex);

        // Update the TextViews with the attributes for the current product
        productNameTextView.setText(productName);
        quantityTextView.setText("Quantity: " + quantity);
        priceTextView.setText("Price: $" + price);

        // Create onclick listener on sellProductImageView
        sellProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogActivity Activity = (CatalogActivity) context;
                Activity.saleProduct(productID, quantity);
            }
        });
    }
}