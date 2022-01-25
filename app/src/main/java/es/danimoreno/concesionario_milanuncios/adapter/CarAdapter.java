package es.danimoreno.concesionario_milanuncios.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.danimoreno.concesionario_milanuncios.R;
import es.danimoreno.concesionario_milanuncios.adapter.viewholder.CarViewHolder;

public class CarAdapter extends RecyclerView.Adapter<CarViewHolder> implements View.OnClickListener {

    private static final String URL = "jdbc:mysql://146.59.237.189:3306/dam208_dmcconcesionario";
    private static final String USER = "dam208_dmc";
    private static final String PASSWORD = "dam208_dmc";

    private String title, price, location, images, ref, km, year;

    private ArrayList<String> carList = new ArrayList<>();
    private Context context;

    private View.OnClickListener listener;


    public CarAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        new InfoAsyncTask().execute();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new CarViewHolder(view);
    }

  //introducimos los valores del view holder
    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        if (!carList.isEmpty()) {

            String carAdDetail = carList.get(position);
            String[] carAdDetails = carAdDetail.split("&");

            title = carAdDetails[0];
            price = "Precio: " + carAdDetails[3] + " €";
            location = carAdDetails[4];
            ref = carAdDetails[7];
            km = carAdDetails[8];
            year = carAdDetails[13];
            images = carAdDetails[5];

            holder.tvRef.setText(ref);
            holder.tvTitle.setText(title);
            holder.tvPrice.setText(price);
            holder.etUbi.setText(location);
            holder.etKm.setText(km);
            holder.tvYear.setText(year);
            String[] img = images.split(";");
            Picasso.get().load(img[0]).into(holder.ivAspect);

        }
    }

  //numero de items
    @Override
    public int getItemCount() {
        if (carList.isEmpty()) {
            return 5;
        }

        return carList.size();
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    //llenamos el array
    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            Map<String, String> info = new HashMap<>();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                String sql = "SELECT * FROM coches";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                int numColumn = statement.getMetaData().getColumnCount();

                if (carList.size() < numColumn) {
                    while (resultSet.next()) {

                        carList.add(
                                resultSet.getString("title")
                                + "&" + resultSet.getString("description")
                                + "&" + resultSet.getString("reference")
                                + "&" + resultSet.getString("price")
                                + "&" + resultSet.getString("location")
                                + "&" + resultSet.getString("images")
                                + "&" + resultSet.getString("linkPage")
                                + "&" + resultSet.getString("fuel")
                                + "&" + resultSet.getString("km")
                                + "&" + resultSet.getString("transmission")
                                + "&" + resultSet.getString("color")
                                + "&" + resultSet.getString("power")
                                + "&" + resultSet.getString("numDoors")
                                + "&" + resultSet.getString("year"));
                    }
                }

            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error leyendo la base de datos", e);
            }
            return info;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {

        }
    }

}
