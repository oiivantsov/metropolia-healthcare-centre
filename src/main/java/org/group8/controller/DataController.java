package org.group8.controller;

import org.group8.dao.DistributionDao;
import org.group8.dao.ProbabilityDao;
import org.group8.dao.SimulationResultsDao;
import org.group8.simulator.model.*;

import java.util.List;

public class DataController implements IDataControlller {

    private final ProbabilityDao probabilityDao = new ProbabilityDao();
    private final DistributionDao distributionDao = new DistributionDao();
    private final SimulationResultsDao simulationResultsDao = new SimulationResultsDao();

    @Override
    public void setProbabilities(double lab, double xray, double treatment) {
        probabilityDao.update(new Probability("LAB", lab));
        probabilityDao.update(new Probability("XRAY", xray));
        probabilityDao.update(new Probability("TREATMENT", treatment));
    }

    @Override
    public void updateDistribution(String event, String distribution, double averageTime) {
        distributionDao.update(new Distribution(event, distribution, averageTime));
    }

    @Override
    public double getProbability(String decisionType) {
        return probabilityDao.find(decisionType).getProbability();
    }

    @Override
    public double getAverageTime(String eventName) {
        return distributionDao.find(eventName).getAverageTime();
    }

    @Override
    public String getDistribution(String event) {
        return distributionDao.find(event).getDistribution();
    }

    @Override
    public Distribution getDistributionObject(String event) {
        return distributionDao.find(event);
    }

    @Override
    public void setDefaultDistributions() {
        distributionDao.update(new Distribution("arrival", "negexp", 15));
        distributionDao.update(new Distribution("check-in", "negexp", 3));
        distributionDao.update(new Distribution("doctor", "negexp", 5));
        distributionDao.update(new Distribution("lab", "negexp", 10));
        distributionDao.update(new Distribution("xray", "negexp", 8));
        distributionDao.update(new Distribution("treatment", "negexp", 12));
    }

    @Override
    public List<SimulationResults> getSimulationResults() {
        return simulationResultsDao.findAll();
    }

    @Override
    public void persistSimulationResults(SimulationResults simulationResults) {
        simulationResultsDao.persist(simulationResults);
    }
}
