package com.example.dietapp.data;

public class NutriAttribs {

    public final static String[] names = new String[] {"Calories", "Protein", "Carbs", "Fat", "Fiber",
            "Vitamin A", "Vitamin B1", "Vitamin B2", "Vitamin B5", "Vitamin B6", "Vitamin B12", "Vitamin C", "Vitamin E", "Vitamin K",
            "Calcium", "Iron", "Magnesium", "Phosphor", "Potassium", "Zinc"
    };

    public final static String[] units = new String[] {"kcal", "g", "g", "g", "g",
            "µg", "mg", "mg", "mg", "mg", "µg", "mg", "mg", "µg",
            "mg", "mg","mg", "mg","mg", "mg"
    };

    public final static NutriAttribs[] general = new NutriAttribs[]{
            new NutriAttribs(names[0], units[0]),
            new NutriAttribs(names[1], units[1]),
            new NutriAttribs(names[2], units[2]),
            new NutriAttribs(names[3], units[3]),
            new NutriAttribs(names[4], units[4])
    };

    public final static NutriAttribs[] vitamins = new NutriAttribs[]{
            new NutriAttribs(names[5], units[5]),
            new NutriAttribs(names[6], units[6]),
            new NutriAttribs(names[7], units[7]),
            new NutriAttribs(names[8], units[8]),
            new NutriAttribs(names[9], units[9]),
            new NutriAttribs(names[10], units[10]),
            new NutriAttribs(names[11], units[11]),
            new NutriAttribs(names[12], units[12]),
            new NutriAttribs(names[13], units[13])

    };

    public final static NutriAttribs[] minerals = new NutriAttribs[]{
            new NutriAttribs(names[14], units[14]),
            new NutriAttribs(names[15], units[15]),
            new NutriAttribs(names[16], units[16]),
            new NutriAttribs(names[17], units[17]),
            new NutriAttribs(names[18], units[18]),
            new NutriAttribs(names[19], units[19]),
    };

    public String name;
    public String unitName;

    public NutriAttribs(String name, String unitName) {
        this.name = name;
        this.unitName = unitName;
    }
}
