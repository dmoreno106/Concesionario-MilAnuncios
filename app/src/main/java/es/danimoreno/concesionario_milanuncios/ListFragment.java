package es.danimoreno.concesionario_milanuncios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.danimoreno.concesionario_milanuncios.adapter.CarAdapter;
import es.danimoreno.concesionario_milanuncios.databinding.FragmentListBinding;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    //iniciamos el recycler view
    private void initialize() {

        RecyclerView rv = binding.carRecicler;
        rv.setLayoutManager(new LinearLayoutManager(getParentFragment().getContext()));
        CarAdapter carAdapter = new CarAdapter(getContext());
        rv.setAdapter(carAdapter);

        carAdapter.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            int ad = rv.getChildAdapterPosition(view);
            bundle.putSerializable("idAd", ad);
            NavHostFragment.findNavController(ListFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}