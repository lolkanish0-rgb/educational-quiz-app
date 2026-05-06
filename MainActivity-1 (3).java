package com.example.tourguide;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
 * MAD Lab Program
 * ─────────────────────────────────────────────────────────────────
 * Aim  : Demonstrate a Tour Guide Android Application showing
 *        Tamil Nadu tourist destinations with:
 *          • RecyclerView list of places
 *          • Category filter buttons (Temple, Beach, Hill, Heritage)
 *          • Live search by place name
 *          • Card click → Toast with place details
 * ─────────────────────────────────────────────────────────────────
 */
public class MainActivity extends AppCompatActivity {

    // ── UI Views ──────────────────────────────────────────────────
    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private EditText     etSearch;
    private TextView     tvCount;
    private Button       btnAll, btnTemple, btnBeach, btnHill, btnHeritage;

    // ── Data ──────────────────────────────────────────────────────
    private List<Place> allPlaces    = new ArrayList<>();
    private List<Place> filteredList = new ArrayList<>();
    private String      activeCategory = "All";

    // ─── onCreate ─────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        recyclerView = findViewById(R.id.recyclerView);
        etSearch     = findViewById(R.id.etSearch);
        tvCount      = findViewById(R.id.tvCount);
        btnAll       = findViewById(R.id.btnAll);
        btnTemple    = findViewById(R.id.btnTemple);
        btnBeach     = findViewById(R.id.btnBeach);
        btnHill      = findViewById(R.id.btnHill);
        btnHeritage  = findViewById(R.id.btnHeritage);

        // Load data
        loadPlaces();

        // Setup RecyclerView
        filteredList.addAll(allPlaces);
        adapter = new PlaceAdapter(this, filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        updateCount();

        // Setup category filter buttons
        setupCategoryButtons();

        // Live search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlaces(activeCategory, s.toString().trim());
            }
        });
    }

    // ══════════════════════════════════════════════════════════════
    //  Load all tourist places (data source)
    // ══════════════════════════════════════════════════════════════
    private void loadPlaces() {

        // Color.parseColor() provides banner color for each category
        int colorTemple  = Color.parseColor("#6A1B9A"); // Purple
        int colorBeach   = Color.parseColor("#00838F"); // Teal
        int colorHill    = Color.parseColor("#2E7D32"); // Green
        int colorHeritage= Color.parseColor("#BF360C"); // Deep Orange

        // Format: Place(name, location, description, category,
        //               emoji, bestTime, entryFee, rating, bannerColor)

        // ── Temples ───────────────────────────────────────────────
        allPlaces.add(new Place(
            "Meenakshi Amman Temple",
            "Madurai, Tamil Nadu",
            "A magnificent Dravidian-style temple dedicated to Goddess Meenakshi, " +
            "famous for its towering gopurams adorned with thousands of sculptures.",
            "Temple", "🛕", "Oct – Mar", "Free", 4.9f, colorTemple));

        allPlaces.add(new Place(
            "Brihadeeswarar Temple",
            "Thanjavur, Tamil Nadu",
            "UNESCO World Heritage Site built by Raja Raja Chola I in 1010 AD. " +
            "A masterpiece of Chola architecture with a 66-metre vimana.",
            "Temple", "🛕", "Nov – Feb", "₹40 (Foreigners)", 4.8f, colorTemple));

        allPlaces.add(new Place(
            "Ramanathaswamy Temple",
            "Rameswaram, Tamil Nadu",
            "One of the Char Dhams of Hinduism, famous for its long ornate corridors " +
            "and 22 sacred theerthams (holy wells).",
            "Temple", "🛕", "Oct – Apr", "Free", 4.7f, colorTemple));

        // ── Beaches ───────────────────────────────────────────────
        allPlaces.add(new Place(
            "Marina Beach",
            "Chennai, Tamil Nadu",
            "The world's second-longest natural urban beach stretching 13 km. " +
            "Famous for its sunrise, street food, and the iconic lighthouse.",
            "Beach", "🏖", "Nov – Feb", "Free", 4.6f, colorBeach));

        allPlaces.add(new Place(
            "Radhanagar Beach",
            "Havelock Island, Andaman",
            "Rated Asia's best beach by Time magazine. Crystal-clear waters, " +
            "white sand, and stunning sunset views.",
            "Beach", "🏖", "Oct – May", "Free", 4.9f, colorBeach));

        allPlaces.add(new Place(
            "Rameswaram Beach",
            "Rameswaram, Tamil Nadu",
            "A serene beach near the famous temple town, ideal for " +
            "early morning walks and watching fishing boats.",
            "Beach", "🏖", "Nov – Mar", "Free", 4.4f, colorBeach));

        // ── Hills ─────────────────────────────────────────────────
        allPlaces.add(new Place(
            "Ooty (Udhagamandalam)",
            "Nilgiris, Tamil Nadu",
            "The 'Queen of Hill Stations'. Famous for its tea gardens, " +
            "Botanical Gardens, Ooty Lake, and the UNESCO toy train.",
            "Hill", "⛰", "Apr – Jun, Sep – Nov", "₹30", 4.7f, colorHill));

        allPlaces.add(new Place(
            "Kodaikanal",
            "Dindigul, Tamil Nadu",
            "The 'Princess of Hill Stations' known for Star-shaped Kodai Lake, " +
            "Silver Cascade Falls, Coaker's Walk, and pleasant climate year-round.",
            "Hill", "⛰", "Apr – Jun", "Free", 4.6f, colorHill));

        allPlaces.add(new Place(
            "Yercaud",
            "Salem, Tamil Nadu",
            "A peaceful hill station surrounded by coffee and orange plantations. " +
            "Popular for boating in Yercaud Lake and the Lady's Seat viewpoint.",
            "Hill", "⛰", "Oct – Jun", "Free", 4.3f, colorHill));

        // ── Heritage ──────────────────────────────────────────────
        allPlaces.add(new Place(
            "Mahabalipuram",
            "Chengalpattu, Tamil Nadu",
            "UNESCO World Heritage Site with rock-cut cave temples, monolithic " +
            "rathas, and the famous Shore Temple by the Bay of Bengal.",
            "Heritage", "🏛", "Nov – Mar", "₹40", 4.8f, colorHeritage));

        allPlaces.add(new Place(
            "Chettinad Mansions",
            "Karaikudi, Tamil Nadu",
            "Magnificent heritage mansions of the Nattukotai Chettiars, famous for " +
            "their unique architecture, antique collections, and Chettinad cuisine.",
            "Heritage", "🏛", "Oct – Mar", "₹100", 4.5f, colorHeritage));

        allPlaces.add(new Place(
            "Gingee Fort",
            "Villupuram, Tamil Nadu",
            "Called the 'Troy of the East' by the British. A massive hill fortress " +
            "complex spread across three hills with stunning panoramic views.",
            "Heritage", "🏛", "Oct – Mar", "₹25", 4.4f, colorHeritage));
    }

    // ══════════════════════════════════════════════════════════════
    //  Category Filter Buttons Setup
    // ══════════════════════════════════════════════════════════════
    private void setupCategoryButtons() {
        btnAll     .setOnClickListener(v -> selectCategory("All",      btnAll));
        btnTemple  .setOnClickListener(v -> selectCategory("Temple",   btnTemple));
        btnBeach   .setOnClickListener(v -> selectCategory("Beach",    btnBeach));
        btnHill    .setOnClickListener(v -> selectCategory("Hill",     btnHill));
        btnHeritage.setOnClickListener(v -> selectCategory("Heritage", btnHeritage));
    }

    private void selectCategory(String category, Button selectedBtn) {
        activeCategory = category;

        // Reset all button styles
        int inactiveColor = Color.parseColor("#3949AB");
        btnAll     .setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
        btnAll     .setTextColor(Color.parseColor("#1A237E"));
        btnTemple  .setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnTemple  .setTextColor(Color.WHITE);
        btnBeach   .setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnBeach   .setTextColor(Color.WHITE);
        btnHill    .setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnHill    .setTextColor(Color.WHITE);
        btnHeritage.setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnHeritage.setTextColor(Color.WHITE);

        // Highlight selected button
        selectedBtn.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(Color.parseColor("#FFD740")));
        selectedBtn.setTextColor(Color.parseColor("#1A237E"));

        // Apply filter
        filterPlaces(category, etSearch.getText().toString().trim());
    }

    // ══════════════════════════════════════════════════════════════
    //  Filter by Category + Search text
    // ══════════════════════════════════════════════════════════════
    private void filterPlaces(String category, String searchText) {
        filteredList.clear();
        for (Place place : allPlaces) {
            boolean matchesCategory = category.equals("All") ||
                                      place.getCategory().equals(category);
            boolean matchesSearch   = searchText.isEmpty() ||
                                      place.getName().toLowerCase()
                                           .contains(searchText.toLowerCase()) ||
                                      place.getLocation().toLowerCase()
                                           .contains(searchText.toLowerCase());
            if (matchesCategory && matchesSearch) {
                filteredList.add(place);
            }
        }
        adapter.updateList(filteredList);
        updateCount();
    }

    // ── Update destination count label ─────────────────────────────
    private void updateCount() {
        int count = filteredList.size();
        tvCount.setText(count + " destination" + (count != 1 ? "s" : "") + " found");
    }
}
