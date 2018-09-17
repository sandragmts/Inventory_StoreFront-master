package com.example.inventory.inventory_storefront;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventory.inventory_storefront.R;
import com.example.inventory.inventory_storefront.data.ProductContract.ProductEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays list of books that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.list)
    ListView productListView;
    @BindView(R.id.empty_view)
    View emptyView;

    /**
     * Identifier for the product data loader
     */
    private static final int PRODUCT_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    ProductCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        ButterKnife.bind(this);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //Set empty layout if there's no items added
        productListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of product data in the Cursor.
        // There is no product data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific product that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link ProductEntry#CONTENT_URI}.
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentProductUri);

                // Launch the {@link EditorActivity} to display the data for the current product.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_PRICE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this, // Parent activity context
                ProductEntry.CONTENT_URI,        // Provider content URI to query
                projection,                   // Columns to include in the resulting Cursor
                null,                // No selection clause
                null,             // No selection arguments
                null);               // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link ProductCursorAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    public void saleProduct(int productID, int quantity) {
        // Perform saleButton ImageView and decrease quantity by one
        quantity--;

        //If product is still available
        if (quantity >= 0) {
            // Create a ContentValues object where column names are the keys,
            // and new product attributes are the values.
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_QUANTITY, quantity);
            Uri updateUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productID);
            int rowsAffected = getContentResolver().update(updateUri, values, null, null);
            if (rowsAffected == 1) {
                Toast.makeText(this, R.string.sell_book_confirm, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.no_product_in_stock, Toast.LENGTH_SHORT).show();
        }
    }
}