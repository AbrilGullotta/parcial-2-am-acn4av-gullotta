package com.davinci.crewup;

import java.util.ArrayList;
import java.util.List;

// Fuente única de planes. Más adelante se reemplaza por Firestore.
public class RepositorioPlanes {

    private static final List<Plan> planes = new ArrayList<>();

    static {
        planes.add(new Plan(1, "🌅 Sunset + Picnic",
                "Bringing a blanket, snacks and good music to watch the sunset from the top of Burleigh Hill. Bring something to share!",
                "Social Hangouts", "Burleigh Hill", "Burleigh Heads National Park entrance",
                "Today 5:00 PM", 5, 8, 0, "Emma", 4.9, R.drawable.plan_burleigh));

        planes.add(new Plan(2, "🚗 Byron Bay Roadtrip",
                "Heading to Byron for the day! Planning to visit the lighthouse, grab lunch and watch the sunset. I've got a spare seat if anyone wants to join!",
                "Roadtrips", "Byron Bay", "Palm Beach",
                "Sat 8:00 AM", 3, 5, 20, "Lucas", 4.7, R.drawable.plan_byron));

        planes.add(new Plan(3, "🏄 Surf session",
                "Morning surf at Snapper Rocks. All levels welcome, I can lend a soft board.",
                "Surf & Sports", "Coolangatta", "Snapper Rocks car park",
                "Sun 6:30 AM", 2, 6, 0, "Mateo", 4.8, R.drawable.plan_surf));

        planes.add(new Plan(4, "🥾 Hiking Springbrook",
                "Twin Falls circuit (4 km) and lunch at the lookout. Moderate level, bring water and good shoes.",
                "Outdoor & Adventure", "Springbrook National Park", "Canyon Lookout car park",
                "Sun 9:00 AM", 4, 6, 10, "Sofia", 5.0, R.drawable.plan_springbrook));

        planes.add(new Plan(5, "🏐 Beach volleyball",
                "Friendly games at the Palm Beach courts. We already have the ball and the net.",
                "Surf & Sports", "Palm Beach", "Palm Beach Surf Club",
                "Wed 4:30 PM", 8, 8, 0, "Jack", 4.6, R.drawable.plan_volleyball));
    }

    public static List<Plan> getPlanes() {
        return planes;
    }

    public static Plan getPlanPorId(int id) {
        for (Plan plan : planes) {
            if (plan.getId() == id) return plan;
        }
        return null;
    }
}