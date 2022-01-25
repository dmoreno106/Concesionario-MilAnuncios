package es.danimoreno.concesionario_milanuncios.adapter.viewholder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.danimoreno.concesionario_milanuncios.R;

//definimos los items del recycler view
public class CarViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivAspect;
    public TextView tvTitle, tvPrice,tvYear,tvRef;
    public EditText etUbi,  etKm;

    public CarViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAspect = itemView.findViewById(R.id.ivAspect);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        etUbi = itemView.findViewById(R.id.etUbi);
       etKm = itemView.findViewById(R.id.etKm);
       tvYear=itemView.findViewById(R.id.tvYear);
       tvRef=itemView.findViewById(R.id.tvRef);
    }

}
