package com.example.dietapp.data;

import android.graphics.Color;

public class NutriAttribs {

    public final static String[] names = new String[] {"Calories", "Protein", "Carbs", "Fat", "Fiber",
            "Vitamin A", "Vitamin B1", "Vitamin B2", "Vitamin B5", "Vitamin B6", "Vitamin B12", "Vitamin C", "Vitamin E", "Vitamin K",
            "Calcium", "Iron", "Magnesium", "Phosphor", "Potassium", "Zinc"
    };

    public final static String[] units = new String[] {"kcal", "g", "g", "g", "g",
            "µg", "mg", "mg", "mg", "mg", "µg", "mg", "mg", "µg",
            "mg", "mg","mg", "mg","mg", "mg"
    };

    public final static float[] minAmounts = new float[] {1500, 65, 175, 32, 30,
            900, 1.2f, 1.3f, 5, 1.3f, 2.4f, 90, 15, 120,
            1000, 8, 420, 700, 4700, 11
    };

    public final static float[] maxAmounts = new float[] {2500, 200, 300, 82, 100,
            2000, 10, 10, 50, 100, 100, 2000, 1000, 10000,
            2500, 45, 4200, 4000, 6500, 40
    };

    public final static int[] colorVals = new int[] {
            Color.rgb(200, 0,0), Color.rgb(200, 50,0), Color.rgb(200, 100,0), Color.rgb(200, 150,0),
            Color.rgb(200, 200,0), Color.rgb(150, 200,0), Color.rgb(100, 200,0), Color.rgb(50, 200,0),
            Color.rgb(0, 200,0), Color.rgb(0, 200,200), Color.rgb(0, 0,200), Color.rgb(100, 0,200),
    };

    public final static NutriAttribs[] general = new NutriAttribs[]{
            new NutriAttribs(names[0], units[0], minAmounts[0], maxAmounts[0]),
            new NutriAttribs(names[1], units[1], minAmounts[1], maxAmounts[1]),
            new NutriAttribs(names[2], units[2], minAmounts[2], maxAmounts[2]),
            new NutriAttribs(names[3], units[3], minAmounts[3], maxAmounts[3]),
            new NutriAttribs(names[4], units[4], minAmounts[4], maxAmounts[4])
    };

    public final static NutriAttribs[] vitamins = new NutriAttribs[]{
            new NutriAttribs(names[5], units[5], minAmounts[5], maxAmounts[5]),
            new NutriAttribs(names[6], units[6], minAmounts[6], maxAmounts[6]),
            new NutriAttribs(names[7], units[7], minAmounts[7], maxAmounts[7]),
            new NutriAttribs(names[8], units[8], minAmounts[8], maxAmounts[8]),
            new NutriAttribs(names[9], units[9], minAmounts[9], maxAmounts[9]),
            new NutriAttribs(names[10], units[10], minAmounts[10], maxAmounts[10]),
            new NutriAttribs(names[11], units[11], minAmounts[11], maxAmounts[11]),
            new NutriAttribs(names[12], units[12], minAmounts[12], maxAmounts[12]),
            new NutriAttribs(names[13], units[13], minAmounts[13], maxAmounts[13])

    };

    public final static NutriAttribs[] minerals = new NutriAttribs[]{
            new NutriAttribs(names[14], units[14], minAmounts[14], maxAmounts[14]),
            new NutriAttribs(names[15], units[15], minAmounts[15], maxAmounts[15]),
            new NutriAttribs(names[16], units[16], minAmounts[16], maxAmounts[16]),
            new NutriAttribs(names[17], units[17], minAmounts[17], maxAmounts[17]),
            new NutriAttribs(names[18], units[18], minAmounts[18], maxAmounts[18]),
            new NutriAttribs(names[19], units[19], minAmounts[19], maxAmounts[19])
    };

    public String name;
    public String unitName;

    public float minAmount;
    public float maxAmount;

    public NutriAttribs(String name, String unitName, float minAmount, float maxAmount) {
        this.name = name;
        this.unitName = unitName;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public static int getColorVal(float val, int nutriIndex) {
        if (val < minAmounts[nutriIndex]) {
            return colorVals[Math.max(0, (int)((val / minAmounts[nutriIndex]) * 10) - 2)];
        } else if (val < maxAmounts[nutriIndex]) {
            return colorVals[8];
        } else {
            return colorVals[Math.min(11, (int)(((val / maxAmounts[nutriIndex]) - 1) * 6) + 9)];
        }
    }
}
