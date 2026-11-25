import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public final class Click_Rush_App extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_ROUND_SECONDS = 10;
    private static final int MAX_ROUND_SECONDS = 3600;

    private static final Color COLOR_BG = new Color(13, 17, 29);
    private static final Color COLOR_PANEL = new Color(23, 30, 47);
    private static final Color COLOR_PANEL_ALT = new Color(30, 39, 59);
    private static final Color COLOR_BORDER = new Color(51, 65, 91);
    private static final Color COLOR_TEXT = new Color(239, 242, 255);
    private static final Color COLOR_MUTED = new Color(154, 166, 190);
    private static final Color COLOR_ACCENT = new Color(139, 92, 246);
    private static final Color COLOR_ACCENT_HOVER = new Color(124, 74, 232);
    private static final Color COLOR_SUCCESS = new Color(52, 211, 153);

    private final CardLayout cards = new CardLayout();
    private final JPanel container = new JPanel(cards);

    // Authentication view
    private final JTextField user_field = new JTextField(20);
    private final JPasswordField pass_field = new JPasswordField(20);
    private final JButton login_btn = new JButton("Sign in");
    private final JButton register_btn = new JButton("Create account");
    private final JLabel auth_status = new JLabel("Connecting to the local database...");
    private boolean db_ready;

    // Arena view
    private final JLabel player_label = new JLabel("-", SwingConstants.CENTER);
    private final JLabel timer_label = new JLabel(DEFAULT_ROUND_SECONDS + "s", SwingConstants.CENTER);
    private final JLabel clicks_label = new JLabel("0", SwingConstants.CENTER);
    private final JLabel status_label = new JLabel("Set your timer, then start clicking.");
    private final JTextField timer_field = new JTextField(String.valueOf(DEFAULT_ROUND_SECONDS), 5);
    private final JButton click_btn = new JButton("CLICK!");
    private final JButton start_btn = new JButton("START ROUND");
    private final JButton board_btn = new JButton("Leaderboard");
    private final JButton logout_btn = new JButton("Sign out");
    private final JProgressBar progress_bar = new JProgressBar(0, 1000);

    // Leaderboard view
    private final DefaultTableModel board_model = new DefaultTableModel(
            new String[] { "Rank", "Username", "Clicks", "Played At" }, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    // Concurrency and session state
    private final transient Object score_lock = new Object();
    private transient Thread timer_thread;
    private volatile boolean round_active;
    private volatile long active_round_id;
    private volatile long round_deadline_nanos;
    private int clicks;
    private int active_player_id = -1;
    private String active_username = "";

    public Click_Rush_App() {
        super("Click Rush Arena");
        apply_theme_defaults();
        configure_fields();
        configure_buttons();

        container.setBackground(COLOR_BG);
        container.add(build_auth_card(), "AUTH");
        container.add(build_play_card(), "PLAY");
        container.add(build_board_card(), "BOARD");
        setContentPane(container);
        cards.show(container, "AUTH");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                round_active = false;
                active_round_id++;
                stop_timer();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(820, 620));
        setPreferredSize(new Dimension(940, 700));
        pack();
        setLocationRelativeTo(null);

        bootstrap_async();
    }

    private static void apply_theme_defaults() {
        UIManager.put("Panel.background", COLOR_BG);
        UIManager.put("OptionPane.background", COLOR_PANEL);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXT);
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 13));
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
    }

    private void configure_fields() {
        style_field(user_field);
        style_field(pass_field);
        style_field(timer_field);
        timer_field.setPreferredSize(new Dimension(82, 42));
        timer_field.setMaximumSize(new Dimension(82, 42));
        pass_field.addActionListener(e -> handle_auth(false));
        user_field.setToolTipText("Your player name");
        pass_field.setToolTipText("Your password");
        timer_field.setToolTipText("Round duration in seconds, from 1 to 3600");
        ((AbstractDocument) timer_field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string != null && string.matches("\\d*") && fb.getDocument().getLength() + string.length() <= 4) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text != null && text.matches("\\d*")
                        && fb.getDocument().getLength() - length + text.length() <= 4) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private void configure_buttons() {
        style_button(login_btn, true);
        style_button(register_btn, false);
        style_button(start_btn, true);
        style_button(board_btn, false);
        style_button(logout_btn, false);
        style_button(click_btn, true);

        click_btn.setFont(new Font("SansSerif", Font.BOLD, 34));
        click_btn.setPreferredSize(new Dimension(500, 190));
        click_btn.setEnabled(false);
        click_btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        click_btn.setToolTipText("Click here, or press Space when the button has focus");

        login_btn.setEnabled(false);
        register_btn.setEnabled(false);
        start_btn.setEnabled(false);
        board_btn.setEnabled(false);
        logout_btn.setEnabled(false);

        progress_bar.setBorderPainted(false);
        progress_bar.setStringPainted(false);
        progress_bar.setForeground(COLOR_ACCENT);
        progress_bar.setBackground(COLOR_PANEL_ALT);
        progress_bar.setPreferredSize(new Dimension(100, 9));
        progress_bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9));
    }

    private JPanel build_auth_card() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BG);
        root.setBorder(BorderFactory.createEmptyBorder(34, 24, 34, 24));

        JPanel content = new JPanel(new BorderLayout(0, 24));
        content.setBackground(COLOR_PANEL);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(34, 42, 30, 42)));
        content.setPreferredSize(new Dimension(460, 500));

        JPanel heading = new JPanel(new GridLayout(0, 1, 0, 8));
        heading.setOpaque(false);
        JLabel eyebrow = make_label("JDBC & THREAD", 11, COLOR_ACCENT, Font.BOLD);
        JLabel title = make_label("CLICK RUSH ARENA", 29, COLOR_TEXT, Font.BOLD);
        JLabel subtitle = make_label("A tiny arena for very serious clicking.", 14, COLOR_MUTED, Font.PLAIN);
        heading.add(eyebrow);
        heading.add(title);
        heading.add(subtitle);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new javax.swing.BoxLayout(form, javax.swing.BoxLayout.Y_AXIS));
        form.add(field_block("USERNAME", user_field));
        form.add(javax.swing.Box.createVerticalStrut(16));
        form.add(field_block("PASSWORD", pass_field));
        form.add(javax.swing.Box.createVerticalStrut(22));

        JPanel actions = new JPanel(new GridLayout(1, 2, 12, 0));
        actions.setOpaque(false);
        // Wire the authentication buttons to their handlers.
        login_btn.addActionListener(e -> handle_auth(false));
        register_btn.addActionListener(e -> handle_auth(true));
        actions.add(login_btn);
        actions.add(register_btn);
        form.add(actions);

        auth_status.setForeground(COLOR_MUTED);
        auth_status.setFont(new Font("SansSerif", Font.PLAIN, 12));
        auth_status.setHorizontalAlignment(SwingConstants.CENTER);

        content.add(heading, BorderLayout.NORTH);
        content.add(form, BorderLayout.CENTER);
        content.add(auth_status, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new java.awt.GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(content);
        root.add(wrapper, BorderLayout.CENTER);
        return root;
    }

    private JPanel build_play_card() {
        JPanel root = new JPanel(new BorderLayout(0, 24));
        root.setBackground(COLOR_BG);
        root.setBorder(BorderFactory.createEmptyBorder(28, 34, 28, 34));

        JPanel heading = new JPanel(new BorderLayout(16, 0));
        heading.setOpaque(false);
        JPanel titles = new JPanel(new GridLayout(0, 1, 0, 5));
        titles.setOpaque(false);
        titles.add(make_label("CLICK RUSH ARENA", 25, COLOR_TEXT, Font.BOLD));
        titles.add(make_label("Ten seconds, one button, no mercy.", 13, COLOR_MUTED, Font.PLAIN));
        heading.add(titles, BorderLayout.WEST);
        heading.add(logout_btn, BorderLayout.EAST);

        JPanel stats = new JPanel(new GridLayout(1, 3, 14, 0));
        stats.setOpaque(false);
        stats.add(stat_card("PLAYER", player_label));
        stats.add(stat_card("TIME LEFT", timer_label));
        stats.add(stat_card("CLICKS", clicks_label));

        JPanel arena = new JPanel(new BorderLayout(0, 16));
        arena.setBackground(COLOR_PANEL);
        arena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)));

        JLabel arena_title = make_label("THE ARENA", 12, COLOR_ACCENT, Font.BOLD);
        arena_title.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel arena_hint = make_label("Put your cursor on the button. Then let it rip.", 13, COLOR_MUTED, Font.PLAIN);
        arena_hint.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel arena_heading = new JPanel(new GridLayout(0, 1, 0, 6));
        arena_heading.setOpaque(false);
        arena_heading.add(arena_title);
        arena_heading.add(arena_hint);

        JPanel button_wrap = new JPanel(new java.awt.GridBagLayout());
        button_wrap.setOpaque(false);
        button_wrap.add(click_btn);

        JPanel arena_footer = new JPanel(new BorderLayout(0, 10));
        arena_footer.setOpaque(false);
        status_label.setForeground(COLOR_MUTED);
        status_label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        status_label.setHorizontalAlignment(SwingConstants.CENTER);
        arena_footer.add(progress_bar, BorderLayout.NORTH);
        arena_footer.add(status_label, BorderLayout.SOUTH);

        arena.add(arena_heading, BorderLayout.NORTH);
        arena.add(button_wrap, BorderLayout.CENTER);
        arena.add(arena_footer, BorderLayout.SOUTH);

        JPanel controls = new JPanel(new BorderLayout(16, 0));
        controls.setOpaque(false);

        JPanel timer_controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timer_controls.setOpaque(false);
        JLabel duration_label = make_label("ROUND LENGTH", 11, COLOR_MUTED, Font.BOLD);
        JLabel seconds_label = make_label("seconds", 12, COLOR_MUTED, Font.PLAIN);
        timer_controls.add(duration_label);
        timer_controls.add(timer_field);
        timer_controls.add(seconds_label);
        timer_controls.add(make_label("(1–3600)", 11, COLOR_MUTED, Font.PLAIN));
        timer_field.addActionListener(e -> start_round());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(board_btn);
        actions.add(start_btn);

        controls.add(timer_controls, BorderLayout.WEST);
        controls.add(actions, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);
        center.add(stats, BorderLayout.NORTH);
        center.add(arena, BorderLayout.CENTER);

        root.add(heading, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(controls, BorderLayout.SOUTH);

        click_btn.addActionListener(e -> handle_click());
        start_btn.addActionListener(e -> start_round());
        board_btn.addActionListener(e -> open_leaderboard());
        logout_btn.addActionListener(e -> logout());
        return root;
    }

    private JPanel build_board_card() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(COLOR_BG);
        root.setBorder(BorderFactory.createEmptyBorder(30, 34, 30, 34));

        JPanel heading = new JPanel(new GridLayout(0, 1, 0, 6));
        heading.setOpaque(false);
        heading.add(make_label("LEADERBOARD", 26, COLOR_TEXT, Font.BOLD));
        heading.add(make_label("The fastest fingers earn their place here.", 13, COLOR_MUTED, Font.PLAIN));

        JTable table = new JTable(board_model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(COLOR_TEXT);
        table.setBackground(COLOR_PANEL);
        table.setSelectionBackground(new Color(67, 56, 120));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(COLOR_BORDER);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setBackground(COLOR_PANEL_ALT);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        scroll.getViewport().setBackground(COLOR_PANEL);

        JButton back_btn = new JButton("BACK TO ARENA");
        style_button(back_btn, true);
        back_btn.addActionListener(e -> cards.show(container, "PLAY"));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setOpaque(false);
        bottom.add(back_btn);

        root.add(heading, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private static JPanel field_block(String label_text, JTextField field) {
        JPanel block = new JPanel(new BorderLayout(0, 8));
        block.setOpaque(false);
        JLabel label = make_label(label_text, 11, COLOR_MUTED, Font.BOLD);
        block.add(label, BorderLayout.NORTH);
        block.add(field, BorderLayout.CENTER);
        return block;
    }

    private static JPanel stat_card(String title, JLabel value) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(COLOR_PANEL_ALT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(13, 16, 13, 16)));
        JLabel caption = make_label(title, 10, COLOR_MUTED, Font.BOLD);
        value.setFont(new Font("SansSerif", Font.BOLD, 21));
        value.setForeground(COLOR_TEXT);
        card.add(caption, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private static JLabel make_label(String value, int size, Color color, int style) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);
        return label;
    }

    private static void style_field(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setForeground(COLOR_TEXT);
        field.setBackground(COLOR_PANEL_ALT);
        field.setCaretColor(COLOR_TEXT);
        field.setSelectionColor(new Color(91, 74, 155));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        field.setPreferredSize(new Dimension(220, 42));
    }

    private static void style_button(JButton button, boolean primary) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(primary ? COLOR_ACCENT : COLOR_PANEL_ALT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? COLOR_ACCENT : COLOR_BORDER),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    private void style_option_buttons(java.awt.Container container) {
        for (java.awt.Component component : container.getComponents()) {

            if (component instanceof JButton button) {
                button.setText("OK");
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
                button.setForeground(Color.WHITE);
                button.setBackground(new Color(124, 77, 255));
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setOpaque(true);
            }

            if (component instanceof java.awt.Container child) {
                style_option_buttons(child);
            }
        }
    }

    private void bootstrap_async() {
        auth_status.setText("Connecting to the local database...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                DB_Helper.bootstrap();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    db_ready = true;
                    login_btn.setEnabled(true);
                    register_btn.setEnabled(true);
                    auth_status.setForeground(COLOR_SUCCESS);
                    auth_status.setText("Database connected. Ready when you are.");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    auth_status.setText("Database setup was interrupted.");
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    auth_status.setForeground(new Color(248, 113, 113));
                    auth_status.setText("Database error: " + cause.getMessage());
                    JOptionPane.showMessageDialog(Click_Rush_App.this,
                            "Database bootstrap failed: " + cause.getMessage(),
                            "Database error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handle_auth(boolean is_register) {
        if (!db_ready) {
            auth_status.setText("The database is not ready yet.");
            return;
        }

        String username = user_field.getText().trim();
        String password = new String(pass_field.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both a username and password.");
            return;
        }

        set_auth_controls_enabled(false);
        auth_status.setForeground(COLOR_MUTED);
        auth_status.setText(is_register ? "Creating your account..." : "Signing you in...");

        SwingWorker<Integer, Void> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                if (is_register) {
                    return DB_Helper.register(username, password);
                }
                return DB_Helper.login(username, password);
            }

            @Override
            protected void done() {
                try {
                    int id = get();
                    if (id < 0) {
                        auth_status.setForeground(new Color(248, 113, 113));
                        auth_status.setText("Those credentials did not match. Try again.");
                        return;
                    }

                    active_player_id = id;
                    active_username = username;
                    pass_field.setText("");
                    player_label.setText(active_username);
                    clicks_label.setText("0");
                    timer_label.setText(timer_field.getText() + "s");
                    status_label.setText("Ready. Start a round whenever you are.");
                    start_btn.setEnabled(true);
                    board_btn.setEnabled(true);
                    logout_btn.setEnabled(true);
                    cards.show(container, "PLAY");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    auth_status.setText("Authentication was interrupted.");
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    if (cause instanceof SQLIntegrityConstraintViolationException) {
                        auth_status.setForeground(new Color(248, 113, 113));
                        auth_status.setText("That username is already taken.");
                    } else {
                        auth_status.setForeground(new Color(248, 113, 113));
                        auth_status.setText("Authentication error: " + cause.getMessage());
                    }
                } finally {
                    set_auth_controls_enabled(db_ready);
                }
            }
        };
        worker.execute();
    }

    private void set_auth_controls_enabled(boolean enabled) {
        login_btn.setEnabled(enabled);
        register_btn.setEnabled(enabled);
    }

    private void start_round() {
        if (active_player_id < 0 || round_active) {
            return;
        }

        int duration;
        try {
            String raw = timer_field.getText().trim();
            if (raw.isEmpty()) {
                throw new NumberFormatException();
            }
            duration = Integer.parseInt(raw);
            if (duration < 1 || duration > MAX_ROUND_SECONDS) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter a whole number from 1 to " + MAX_ROUND_SECONDS + " seconds.",
                    "Invalid round length", JOptionPane.WARNING_MESSAGE);
            timer_field.requestFocusInWindow();
            timer_field.selectAll();
            return;
        }

        stop_timer();
        synchronized (score_lock) {
            clicks = 0;
        }

        final long run_id = ++active_round_id;
        final long duration_nanos = duration * 1_000_000_000L;
        final long deadline = System.nanoTime() + duration_nanos;
        round_deadline_nanos = deadline;
        round_active = true;

        clicks_label.setText("0");
        timer_label.setText(duration + "s");
        progress_bar.setValue(1000);
        status_label.setText("Round live! Click as fast as you can.");

        timer_field.setEnabled(false);
        start_btn.setEnabled(false);
        start_btn.setText("ROUND IN PROGRESS");
        click_btn.setEnabled(true);
        board_btn.setEnabled(false);
        logout_btn.setEnabled(false);

        SwingUtilities.invokeLater(this::teleport_cursor_to_click_area);

        timer_thread = new Thread(() -> run_countdown(run_id, duration, duration_nanos, deadline),
                "ClickRush-Timer");
        timer_thread.setDaemon(true);
        timer_thread.start();
    }

    private void run_countdown(long run_id, int duration, long duration_nanos, long deadline) {
        int previous_seconds = -1;
        while (round_active && active_round_id == run_id) {
            long remaining = deadline - System.nanoTime();
            int seconds_left = (int) Math.max(0L, (remaining + 999_999_999L) / 1_000_000_000L);
            int progress = (int) Math.max(0L, Math.min(1000L, remaining * 1000L / duration_nanos));

            if (seconds_left != previous_seconds) {
                previous_seconds = seconds_left;
                SwingUtilities.invokeLater(() -> {
                    if (round_active && active_round_id == run_id) {
                        timer_label.setText(seconds_left + "s");
                    }
                });
            }

            SwingUtilities.invokeLater(() -> {
                if (round_active && active_round_id == run_id) {
                    progress_bar.setValue(progress);
                }
            });

            if (remaining <= 0L) {
                break;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        if (round_active && active_round_id == run_id) {
            SwingUtilities.invokeLater(() -> finish_round(run_id));
        }
    }

    private void handle_click() {
        if (!round_active) {
            return;
        }
        if (System.nanoTime() >= round_deadline_nanos) {
            finish_round(active_round_id);
            return;
        }

        int current;
        synchronized (score_lock) {
            clicks++;
            current = clicks;
        }
        clicks_label.setText(String.valueOf(current));
    }

    private void finish_round(long run_id) {
        if (!round_active || active_round_id != run_id) {
            return;
        }

        round_active = false;
        click_btn.setEnabled(false);
        timer_field.setEnabled(true);
        start_btn.setEnabled(true);
        start_btn.setText("START ROUND");
        board_btn.setEnabled(true);
        logout_btn.setEnabled(true);
        progress_bar.setValue(0);
        timer_label.setText("0s");
        status_label.setText("Time! Saving your score...");

        final int final_clicks;
        final int saved_player_id = active_player_id;
        synchronized (score_lock) {
            final_clicks = clicks;
        }

        SwingWorker<Void, Void> saver = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                DB_Helper.save_run(saved_player_id, final_clicks);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    status_label.setText("Score saved: " + final_clicks + " clicks.");
                    JOptionPane option_pane = new JOptionPane(
                            "Round complete! You scored " + final_clicks + " clicks.",
                            JOptionPane.INFORMATION_MESSAGE);

                    javax.swing.JDialog dialog = option_pane.createDialog(
                            Click_Rush_App.this, "Round complete");

                    style_option_buttons(option_pane);

                    dialog.setVisible(true);
                    if (active_player_id == saved_player_id) {
                        open_leaderboard();
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    status_label.setText("Score saving was interrupted.");
                } catch (ExecutionException ex) {
                    status_label.setText("Could not save the score.");
                    JOptionPane.showMessageDialog(Click_Rush_App.this,
                            "Save failed: " + ex.getCause().getMessage(),
                            "Save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        saver.execute();
    }

    private void teleport_cursor_to_click_area() {
        if (!click_btn.isShowing()) {
            return;
        }
        click_btn.requestFocusInWindow();
        try {
            Point target = new Point(click_btn.getWidth() / 2, click_btn.getHeight() / 2);
            SwingUtilities.convertPointToScreen(target, click_btn);
            new Robot().mouseMove(target.x, target.y);
        } catch (AWTException | HeadlessException | IllegalComponentStateException | SecurityException ex) {
            status_label.setText("Cursor positioning is unavailable. Click the button manually.");
        }
    }

    private void open_leaderboard() {
        cards.show(container, "BOARD");
        board_model.setRowCount(0);
        SwingWorker<List<Object[]>, Void> loader = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                return DB_Helper.fetch_top_ten();
            }

            @Override
            protected void done() {
                try {
                    for (Object[] row : get()) {
                        board_model.addRow(row);
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ex) {
                    JOptionPane.showMessageDialog(Click_Rush_App.this,
                            "Leaderboard load failed: " + ex.getCause().getMessage(),
                            "Leaderboard error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        loader.execute();
    }

    private void logout() {
        round_active = false;
        active_round_id++;
        stop_timer();
        click_btn.setEnabled(false);
        timer_field.setEnabled(true);
        start_btn.setEnabled(false);
        start_btn.setText("START ROUND");
        board_btn.setEnabled(false);
        logout_btn.setEnabled(false);
        active_player_id = -1;
        active_username = "";
        player_label.setText("-");
        timer_label.setText(timer_field.getText() + "s");
        clicks_label.setText("0");
        progress_bar.setValue(0);
        auth_status.setText("You have been signed out.");
        cards.show(container, "AUTH");
    }

    private void stop_timer() {
        if (timer_thread != null && timer_thread.isAlive()) {
            timer_thread.interrupt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Click_Rush_App app = new Click_Rush_App();
            app.setVisible(true);
        });
    }
}
