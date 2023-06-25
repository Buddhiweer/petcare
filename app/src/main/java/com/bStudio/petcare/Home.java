package com.bStudio.petcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProjectModel> recycleList;
    SwipeRefreshLayout swipeRefreshLayout;
    RadioGroup filterRadioGroup; // Add this line

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ProjectAdapter recyclerAdapter;

    BottomAppBar bottomAppBar;
    FloatingActionButton actionButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        bottomAppBar = findViewById(R.id.bottomAppbar);
        actionButton = findViewById(R.id.add_btn);
        recycleList = new ArrayList<>();
        filterRadioGroup = findViewById(R.id.filterRadioGroup); // Add this line

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Pets");

        recyclerAdapter = new ProjectAdapter(recycleList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Grid view
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);

        filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // Add this block
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String selectedFilter = getSelectedFilter();
                filterItems(selectedFilter);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPets();
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(Home.this, MainActivity.class));
                }
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homePageIntent = new Intent(Home.this, UploadPost.class);
                homePageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(Home.this, UploadPost.class));
                Toast.makeText(Home.this, "Add item", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

        loadPets();
    }

    private String getSelectedFilter() {
        int checkedRadioButtonId = filterRadioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.fillterAll:
                return "";
            case R.id.fillterDogs:
                return "Dog";
            case R.id.fillterCats:
                return "Cat";
            case R.id.filtterBirds:
                return "Bird";
            case R.id.fillterOthers:
                return "Other";
            default:
                return "";
        }
    }

    private void filterItems(String category) {
        ArrayList<ProjectModel> filteredList = new ArrayList<>();
        if (category.isEmpty()) {
            filteredList.addAll(recycleList); // Show all items
        } else {
            for (ProjectModel projectModel : recycleList) {
                if (projectModel.getPettype().equals(category)) {
                    filteredList.add(projectModel); // Add items that match the selected category
                }
            }
        }
        recyclerAdapter.filterList(filteredList);
    }

    private void loadPets() {
        swipeRefreshLayout.setRefreshing(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recycleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProjectModel projectModel = snapshot.getValue(ProjectModel.class);
                    recycleList.add(projectModel);
                }
                Collections.reverse(recycleList);
                recyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
