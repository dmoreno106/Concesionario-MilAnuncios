package es.danimoreno.concesionario_milanuncios;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import es.danimoreno.concesionario_milanuncios.databinding.FragmentCarBinding;

public class CarFragment extends Fragment {

    private static final String URL = "jdbc:mysql://146.59.237.189:3306/dam208_dmcconcesionario";
    private static final String USER = "dam208_dmc";
    private static final String PASSWORD = "dam208_dmc";



    private FragmentCarBinding binding;
    private TextView tvTitle, tvPrice, tvRef, tvColor, tvDesc, tvLink, tvYear, tvPower, tvNDoors, tvCambio;
    private ImageView iv;
    private String imageLink;
    private EditText etUbi,   etFuel, etKm;
    private Button btLeft, btRight;
    private String link, combustible, km, cambio, color, potencia, numDoors, year,title, desc, ref, price, ubi, images = "";
    private int numImg = 1;

//constructor de la view
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCarBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

  //inicializamos la vista
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        new InfoAsyncTask().execute();
    }

   //inicializamos los campos
    private void initialize() {

        tvTitle = binding.tvTitle;
        tvPrice = binding.tvPrice;
        tvRef = binding.tvRef;
        tvDesc = binding.tvDesc;
        tvLink = binding.tvLink;
        iv = binding.imageView;
        etUbi = binding.etUbi;
        etFuel = binding.etFuel;
        etKm = binding.etKm;
        tvYear = binding.tvYear;
        tvCambio = binding.etCambio;
        tvColor = binding.tvColor;
        tvPower = binding.tvPower;
        tvNDoors = binding.tvNDoors;
        btLeft = binding.btLeft;
        btRight = binding.btRight;

    }

   //lleva a la pagina de mil anuncios de el post actual
    private void AdListener(String link) {
        tvLink.setOnClickListener(view -> {
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

  //boton izquierdo pasa a la anterior imagen hasta llegar a la primera
    private void btIzqListener() {

        btLeft.setOnClickListener(view -> {

            if (imageLink.contains("_1.jpg")) {

                btLeft.setEnabled(false);

                Toast toast = Toast.makeText(getContext(), "No hay más imágenes", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.getBackground().setColorFilter(getResources().getColor(R.color.principal), PorterDuff.Mode.SRC_IN);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                toast.show();

            } else {

                btLeft.setEnabled(true);
                btRight.setEnabled(true);

                int newNumImg = numImg - 1;

                imageLink = imageLink.replace("_" + numImg + ".jpg", "_" + newNumImg + ".jpg");
                Picasso.get().load(imageLink).into(iv);

                numImg = newNumImg;

            }

        });
    }

  //boton derecho pasa a la siguiente imagen hasta que no encuentre más
    private void btDerListener() {
        btRight.setOnClickListener(view -> {
            String[] img = images.split(";");
            int totalImgs = img.length;
            if (imageLink.contains("_" + totalImgs + ".jpg")) {
                btRight.setEnabled(false);
            } else {
                btLeft.setEnabled(true);
                btRight.setEnabled(true);
                int newNumImg = numImg + 1;
                imageLink = imageLink.replace("_" + numImg + ".jpg", "_" + newNumImg + ".jpg");
                Picasso.get().load(imageLink).into(iv);
                numImg = newNumImg;

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

 //llena los campos con los datos de la DDBB
    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            Map<String, String> info = new HashMap<>();

            String ad = String.valueOf((Integer) getArguments().getSerializable("idAd"));

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                String sql = "SELECT * FROM coches LIMIT " + ad + ",1";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    ubi = resultSet.getString("localizacion");
                    images = resultSet.getString("imagenes");
                    link = resultSet.getString("url");
                    combustible = resultSet.getString("combustible");
                    km = resultSet.getString("km");
                    cambio = resultSet.getString("cambio");
                    color = resultSet.getString("color");
                    potencia = resultSet.getString("potencia");
                    numDoors = resultSet.getString("npuertas");
                    year = resultSet.getString("year");
                    title = resultSet.getString("titulo");
                    desc = resultSet.getString("descripcion");
                    ref = resultSet.getString("ref");
                    price = resultSet.getString("precio");

                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "No se han podido leer los datos", e);
            }
            return info;
        }


        @Override
        protected void onPostExecute(Map<String, String> result) {
            FillFields();
        }
    }

 //colocamos en los campos los datos obtenidos
    public void FillFields() {

        tvTitle.setText(title);
        tvPrice.setText("Precio: " + price + " €");
        tvRef.setText("Ref: " + ref);
        //tvAdDescription.setText(getString(R.string.de) + "\n\n" + description);
        tvLink.setText("Pagina del anuncio");
        String[] img = images.split(";");
        Picasso.get().load(img[0]).into(iv);
        imageLink = img[0];
        etUbi.setText(ubi);
        etFuel.setText(combustible);
        etKm.setText(km);
        tvYear.setText(year);
        tvCambio.setText(cambio);
        tvColor.setText(color);
        tvPower.setText(potencia);
        tvNDoors.setText(numDoors);

        //llamamos a  los metodos de los listeners
        btIzqListener();
        btDerListener();
        AdListener(link);

    }

}