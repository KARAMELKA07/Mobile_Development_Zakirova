package ru.mirea.zakirovakr.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText editTextName, editTextAge, editTextHobby;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextHobby = view.findViewById(R.id.editTextHobby);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        preferences = requireActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);

        loadProfileData();

        buttonSave.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void saveProfileData() {
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String hobby = editTextHobby.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || hobby.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", name);
        editor.putInt("AGE", Integer.parseInt(age));
        editor.putString("HOBBY", hobby);
        editor.apply();

        Toast.makeText(requireContext(), "Профиль сохранён", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        String name = preferences.getString("NAME", "");
        int age = preferences.getInt("AGE", 0);
        String hobby = preferences.getString("HOBBY", "");

        editTextName.setText(name);
        editTextAge.setText(age == 0 ? "" : String.valueOf(age));
        editTextHobby.setText(hobby);
    }
}