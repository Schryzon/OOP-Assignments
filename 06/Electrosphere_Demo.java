import java.util.Scanner;

/**
 * Electrosphere_Demo - Simulation harness for the 2040 Usean Corporate War.
 * Demonstrates the full integration of OOP Modules 01 through 06:
 * - Abstract Classes and Interfaces
 * - Single and Multilevel Inheritance
 * - Dynamic Dispatch (vtable) and Method Overloading
 * - State Encapsulation and Invariant Guarding
 * - Fixed Arrays and Dynamic Collections
 * - Interactive COFFIN Mission Console (Scanner)
 */
public class Electrosphere_Demo {

    public static void main(String[] args){
        Corporate_Air_Fleet neucom_fleet = build_neucom_fleet();
        Corporate_Air_Fleet general_resource_fleet = build_general_resource_fleet();

        if(args.length > 0 && (args[0].equals("-i") || args[0].equals("--interactive") || args[0].equals("menu"))){
            run_interactive_console(neucom_fleet, general_resource_fleet);
        }else{
            run_automated_simulation(neucom_fleet, general_resource_fleet);
            System.out.println("\nTip: Launch with '-i' or '--interactive' flag to enter the interactive COFFIN mission console.");
        }
    }

    private static Corporate_Air_Fleet build_neucom_fleet(){
        Corporate_Air_Fleet fleet = new Corporate_Air_Fleet("NEUCOM AERO WING", Corporate_Faction.NEUCOM_INC, 4);

        Delphinus_Series r101 = new Delphinus_Series("R-101 Delphinus #1", 3100.0, 48, 0.85);
        XR900_Geopelia geopelia = new XR900_Geopelia(3800.0, 60);

        fleet.station_aircraft(r101);
        fleet.station_aircraft(geopelia);

        // Reserve reinforcements
        Delphinus_Series r102 = new Delphinus_Series("R-102 Delphinus #2", 3400.0, 56, 0.92);
        fleet.reinforce_reserve(r102);

        return fleet;
    }

    private static Corporate_Air_Fleet build_general_resource_fleet(){
        Corporate_Air_Fleet fleet = new Corporate_Air_Fleet("GR DEFENSE DIVISION", Corporate_Faction.GENERAL_RESOURCE, 4);

        Su43_Berkut berkut = new Su43_Berkut(Corporate_Faction.GENERAL_RESOURCE, 2900.0, 40);
        Night_Raven raven = new Night_Raven(4200.0, 72);

        fleet.station_aircraft(berkut);
        fleet.station_aircraft(raven);

        return fleet;
    }

    private static void run_automated_simulation(Corporate_Air_Fleet neucom_fleet, Corporate_Air_Fleet gr_fleet){
        System.out.println("==================================================");
        System.out.println("   ELECTROSPHERE TACTICAL SIMULATION ENGINE (2040) ");
        System.out.println("   Dr. Simon Orestes Cohen Sandbox Environment    ");
        System.out.println("==================================================");
        System.out.println();

        // -- Neural Pilot Association (Has-A Relationship) --
        System.out.println(">>> Initializing COFFIN Neural Operators...");
        Coffin_Operator rena = new Coffin_Operator("Rena Hirose", Corporate_Faction.UPEO_SARF, 0.98);
        Combat_Aircraft r101 = neucom_fleet.get_aircraft(0);
        rena.assign_aircraft(r101);
        rena.sync_neural_link();
        System.out.println();

        // -- Method Overloading Demonstration --
        System.out.println(">>> Executing Weapon Platform Overloading Protocol...");
        System.out.println("-- Overload 1: Default Weapon Discharge --");
        r101.fire_weapon();

        System.out.println("\n-- Overload 2: Multi-Missile Salvo --");
        r101.fire_weapon(4);

        System.out.println("\n-- Overload 3: Radar-Guided Long-Range Lock --");
        r101.fire_weapon("DISION-DEADCOPY-ALPHA", 18.5);
        System.out.println();

        // -- Sublimation Interface Protocol --
        System.out.println(">>> Verifying Sublimation Protocol on X-49 Night Raven...");
        Combat_Aircraft raven_craft = gr_fleet.get_aircraft(1);
        if(raven_craft instanceof Sublimation_Capable sublim_craft){
            System.out.println("Initial Sublimation Status: " + sublim_craft.is_sublimated());
            sublim_craft.upload_consciousness("ABYSSAL-DISION-001");
            System.out.println("Post-Upload Sublimation Status: " + sublim_craft.is_sublimated());
            System.out.println("Neural Synchrony: " + String.format("%.2f%%", sublim_craft.get_neural_sync_ratio() * 100));

            if(raven_craft instanceof Night_Raven specific_raven){
                specific_raven.fire_laser_cannon(35.0);
            }
        }
        System.out.println();

        // -- Autonomous UAV AI Autopilot Protocol --
        System.out.println(">>> Verifying Autonomous UAV Protocol on XR-900 Geopelia...");
        Combat_Aircraft geopelia_craft = neucom_fleet.get_aircraft(1);
        if(geopelia_craft instanceof Autonomous_UAV uav_craft){
            System.out.println("Is Unmanned Craft: " + uav_craft.is_unmanned());
            uav_craft.engage_ai_autopilot("NEMO-INTERCEPT-MATRIX-ALPHA");
            uav_craft.execute_evasive_pattern();
        }
        System.out.println();

        // -- Fleet Scramble and Polymorphic Traversal --
        System.out.println(">>> Scrambling Corporate Air Fleets (Polymorphic vtable dispatch)...");
        neucom_fleet.scramble_fleet_sorties();
        gr_fleet.scramble_fleet_sorties();

        System.out.println("\n==================================================");
        System.out.println("SIMULATION METRICS SUMMARY");
        System.out.println("Neucom Fleet Firepower Rating : " + neucom_fleet.calculate_fleet_firepower());
        System.out.println("GR Fleet Firepower Rating     : " + gr_fleet.calculate_fleet_firepower());
        System.out.println("==================================================");
    }

    private static void run_interactive_console(Corporate_Air_Fleet neucom_fleet, Corporate_Air_Fleet gr_fleet){
        Scanner scanner = new Scanner(System.in);
        boolean active = true;

        System.out.println("==================================================");
        System.out.println("     INTERACTIVE COFFIN MISSION CONSOLE (2040)    ");
        System.out.println("==================================================");

        while(active){
            System.out.println("\nElectrosphere Operational Menu:");
            System.out.println("1. Scramble Neucom Fleet Sorties");
            System.out.println("2. Scramble General Resource Fleet Sorties");
            System.out.println("3. Fire Weapon (Demonstrate Overloading on Aircraft)");
            System.out.println("4. Upload Consciousness to X-49 (Sublimation Protocol)");
            System.out.println("5. Trigger XR-900 Autonomous UAV Autopilot (Nemo)");
            System.out.println("6. Run Full Simulation Benchmark");
            System.out.println("0. Disconnect from Electrosphere");
            System.out.print("Enter selection: ");

            if(!scanner.hasNextInt()){
                break;
            }
            int choice = scanner.nextInt();

            if(choice == 0){
                active = false;
                System.out.println("Disconnecting neuro-link from Electrosphere... Goodbye.");
            }else if(choice == 1){
                neucom_fleet.scramble_fleet_sorties();
            }else if(choice == 2){
                gr_fleet.scramble_fleet_sorties();
            }else if(choice == 3){
                Combat_Aircraft craft = neucom_fleet.get_aircraft(0);
                System.out.println("\nSelect Fire Mode for " + craft.get_model_name() + ":");
                System.out.println("  1. Single Munition Burst");
                System.out.println("  2. Multi-Missile Salvo");
                System.out.println("  3. Radar-Guided Frequency Lock");
                System.out.print("  Choice: ");
                int fire_mode = scanner.nextInt();

                if(fire_mode == 1){
                    craft.fire_weapon();
                }else if(fire_mode == 2){
                    System.out.print("  Enter salvo size: ");
                    int salvo = scanner.nextInt();
                    craft.fire_weapon(salvo);
                }else if(fire_mode == 3){
                    System.out.print("  Enter target designation: ");
                    String target = scanner.next();
                    System.out.print("  Enter lock frequency (GHz): ");
                    double freq = scanner.nextDouble();
                    craft.fire_weapon(target, freq);
                }
            }else if(choice == 4){
                Combat_Aircraft raven_craft = gr_fleet.get_aircraft(1);
                if(raven_craft instanceof Sublimation_Capable sc){
                    System.out.print("Enter pilot callsign for digital Sublimation: ");
                    String pilot_id = scanner.next();
                    sc.upload_consciousness(pilot_id);
                }
            }else if(choice == 5){
                Combat_Aircraft geopelia = neucom_fleet.get_aircraft(1);
                if(geopelia instanceof Autonomous_UAV uav){
                    System.out.print("Enter AI mission profile: ");
                    String profile = scanner.next();
                    uav.engage_ai_autopilot(profile);
                    uav.execute_evasive_pattern();
                }
            }else if(choice == 6){
                run_automated_simulation(neucom_fleet, gr_fleet);
            }
        }
    }
}
