package jp.co.recruit.hadoop.ruleselect;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RuleSelect" Node.
 * File row filter. 
 *
 * @author Krishna
 */
public class RuleSelectNodeFactory 
        extends NodeFactory<RuleSelectNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleSelectNodeModel createNodeModel() {
        return new RuleSelectNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<RuleSelectNodeModel> createNodeView(final int viewIndex,
            final RuleSelectNodeModel nodeModel) {
        return new RuleSelectNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new RuleSelectNodeDialog();
    }

}

