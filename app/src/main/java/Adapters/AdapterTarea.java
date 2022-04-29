package Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;




public class AdapterTarea extends BaseAdapter {

    private Context contexto;
    private LayoutInflater inflater;
    private List<Integer> id;
    private List<String> nombre;
    private List<String> desc;

    public AdapterTarea(Context contexto, List<Integer> id, List<String> nombre, List<String> desc) {
        this.contexto = contexto;
        this.inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id = id;
        this.nombre = nombre;
        this.desc = desc;
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int i) {
        return nombre.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.item,null);
        TextView tvNombre= (TextView) view.findViewById(R.id.nombreTarea);
        TextView tvId=(TextView) view.findViewById(R.id.idTarea);
        TextView tvDesc= (TextView) view.findViewById(R.id.descTarea);
        tvNombre.setText(nombre.get(i));
        tvId.setText(String.valueOf(id.get(i)));
        tvDesc.setText(desc.get(i));
        return view;
    }
}