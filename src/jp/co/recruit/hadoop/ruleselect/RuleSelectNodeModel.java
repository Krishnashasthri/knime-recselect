package jp.co.recruit.hadoop.ruleselect;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model for the business rule node. It takes the user-defined rules
 * and assigns the outcome of the first matching rule to the new cell.
 *
 * @author Krishna
 */
public class RuleSelectNodeModel extends NodeModel {
    private final RuleSelectSettings m_settings = new RuleSelectSettings();

    /**
     * Creates a new model.
     */
    public RuleSelectNodeModel() {
        super(1, 1);
    }

    /**
     * Parses all rules in the settings object.
     *
     * @param spec the spec of the table on which the rules are applied.
     * @return a list of parsed rules
     * @throws ParseException if a rule cannot be parsed
     */
    private List<Rule> parseRules(final DataTableSpec spec)
            throws ParseException {
        ArrayList<Rule> rules = new ArrayList<Rule>();

        for (String s : m_settings.rules()) {
            rules.add(new Rule(s +"\"", spec));
        }

        return rules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        try {
            parseRules(inSpecs[0]);
        } catch (ParseException ex) {
            throw new InvalidSettingsException(ex);
        }

        return new DataTableSpec[]{inSpecs[0]};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
            List<Rule> rules = parseRules(inData[0].getDataTableSpec());
       
         
              DataTableSpec inputTableSpec = inData[0].getDataTableSpec();
        	  DataColumnSpec[] outputColumnSpecs = new DataColumnSpec[inputTableSpec.getNumColumns()];
        	  
              for (int i = 0; i < inputTableSpec.getNumColumns(); i++) {
        	    DataColumnSpec columnSpec = inputTableSpec.getColumnSpec(i);    
        	    outputColumnSpecs[i] = columnSpec;
              } 
       	  
        	  DataTableSpec outputTableSpec = new DataTableSpec(outputColumnSpecs);
        	  BufferedDataContainer outputContainer = exec.createDataContainer(outputTableSpec);

        	  CloseableRowIterator it = inData[0].iterator();
        	  int rowNumber = 1;

        	  while (it.hasNext()) {
   
        	    DataRow inputRow = it.next();
        	    RowKey key = inputRow.getKey();	
        	    DataCell[] cells = new DataCell[inputRow.getNumCells()];

        	    for (int i = 0; i < inputRow.getNumCells(); i++) {
        	      cells[i] = inputRow.getCell(i);
        	    }
       	  
          	    for (Rule r : rules) {
                    if (r.matches(inputRow)) {
                  	  DataRow outputRow = new DefaultRow(key, cells);
               	      outputContainer.addRowToTable(outputRow);
                    }
                }
 	   
          	    exec.checkCanceled();
        	    exec.setProgress(rowNumber / (double)inData[0].getRowCount(), "Adding row " + rowNumber);

        	    rowNumber++;
        	  }
     	 
      outputContainer.close();
   	  BufferedDataTable outputTable = outputContainer.getTable();
   	  return new BufferedDataTable[]{outputTable};
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_settings.loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        m_settings.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        RuleSelectSettings s = new RuleSelectSettings();
        s.loadSettings(settings);
    }
}

