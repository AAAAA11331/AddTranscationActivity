package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class TransactionListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TransactionAdapter adapter;
    DatabaseHelper dbHelper;
    MaterialButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcation_list);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(loadTransactions());
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransactionActivity.class));
        });
    }

    private List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("transactions", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            transactions.add(new Transaction(id, type, category, amount));
        }
        cursor.close();
        db.close();
        return transactions;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateTransactions(loadTransactions());
    }
}

class Transaction {
    long id;
    String type;
    String category;
    double amount;

    Transaction(long id, String type, String category, double amount) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }
}

class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;

    TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transcation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvType.setText(transaction.type);
        holder.tvCategory.setText(transaction.category);
        holder.tvAmount.setText(String.format("%.2f", transaction.amount));
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddTransactionActivity.class);
            intent.putExtra("transaction_id", transaction.id);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    void updateTransactions(List<Transaction> newTransactions) {
        transactions = newTransactions;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvCategory, tvAmount;
        MaterialButton btnEdit;

        ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}