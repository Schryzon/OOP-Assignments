/**
 * Flight_Control_Contract - Avionics flight dynamics interface.
 * Defines statutory maneuverability and speed regulation contracts.
 */
public interface Flight_Control_Contract {

    boolean execute_maneuver(double g_force);

    void set_thrust(double throttle_percent);

    double get_current_speed();
}
