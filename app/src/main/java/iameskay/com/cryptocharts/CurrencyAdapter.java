package iameskay.com.cryptocharts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private static ArrayList<Currency> currencies = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCurrencyName;
        TextView mPrice;
        ImageView mFavourite;

        ViewHolder(LinearLayout l) {
            super(l);
            mCurrencyName = l.findViewById(R.id.currency_name);
            mPrice = l.findViewById(R.id.price);
            mFavourite = l.findViewById(R.id.favourite);
        }
    }

    CurrencyAdapter(ArrayList<Currency> c) {
        currencies = c;
    }

    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout currencyView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_currency, parent, false);
        return new ViewHolder(currencyView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCurrencyName.setText(currencies.get(position).getName());
        String price = "$" + String.format("%.2f", currencies.get(position).getPrice());
        holder.mPrice.setText(price);
        if (currencies.get(position).getFavourite()) {
            holder.mFavourite.setImageResource(R.drawable.ic_favourite);
        } else {
            holder.mFavourite.setImageResource(R.drawable.ic_unfavourite);
        }

        final int p = position;
        holder.mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currencies.get(p).getFavourite()) {
                    currencies.set(p, currencies.get(p).setFavourite(false));
                    Currency temp = currencies.remove(p);
                    currencies.add(temp);
                } else {
                    currencies.set(p, currencies.get(p).setFavourite(true));
                    Currency temp = currencies.remove(p);
                    currencies.add(0, temp);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }
}
