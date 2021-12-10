package il.ac.hit.model.tests;

import il.ac.hit.model.CostManagerModel;

import static org.junit.Assert.*;

public class CostManagerModelTest
{
    private CostManagerModel m_CostManagerModel;

    @org.junit.Before
    public void setUp() throws Exception
    {
        m_CostManagerModel = new CostManagerModel();
    }

    @org.junit.After
    public void tearDown() throws Exception
    {
        m_CostManagerModel = null;
    }
}