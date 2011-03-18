package jp.co.recruit.hadoop.ruleselect;

import java.util.ArrayList;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This class contains all settings for the business rule node.
 *
 * @author Krishna
 */
public class RuleSelectSettings {

    private final ArrayList<String> m_rules = new ArrayList<String>();

    /**
     * Adds a rule.
     *
     * @param rule the rule string
     */
    public void addRule(final String rule) {
        m_rules.add(rule);
    }

    /**
     * Removes all rules.
     */
    public void clearRules() {
        m_rules.clear();
    }

    /**
     * Returns an iterable over all rules.
     *
     * @return an iterable over all rules
     */
    public Iterable<String> rules() {
        return m_rules;
    }

    /**
     * Loads the settings from the settings object.
     *
     * @param settings a node settings object
     * @throws InvalidSettingsException if some settings are missing
     */
    public void loadSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        String[] rules = settings.getStringArray("rules");

        m_rules.clear();
        for (String r : rules) {
            m_rules.add(r);
        }
    }

    /**
     * Loads the settings from the settings object for use in the dialog, i.e.
     * default values are used for missing settings.
     *
     * @param settings a node settings object
     */
    public void loadSettingsForDialog(final NodeSettingsRO settings) {
        String[] rules = settings.getStringArray("rules", new String[0]);

        m_rules.clear();
        for (String r : rules) {
            m_rules.add(r);
        }
     }

    /**
     * Saves the setting into the node settings object.
     *
     * @param settings a node settings object
     */
    public void saveSettings(final NodeSettingsWO settings) {
        settings.addStringArray("rules", m_rules.toArray(new String[m_rules
                .size()]));
     }
}

