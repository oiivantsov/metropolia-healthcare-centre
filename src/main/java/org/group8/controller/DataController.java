package org.group8.controller;

import org.group8.dao.DistributionDao;
import org.group8.dao.ProbabilityDao;
import org.group8.dao.SimulationResultsDao;
import org.group8.simulator.model.*;

import java.util.List;

/**
 * The DataController class is for managing data operations related to probabilities, distributions, and simulation results.
 */
public class DataController implements IDataControlller {
    private final ProbabilityDao probabilityDao = new ProbabilityDao();
    private final DistributionDao distributionDao = new DistributionDao();
    private final SimulationResultsDao simulationResultsDao = new SimulationResultsDao();

    /**
     * Sets the probabilities for lab, x-ray, and treatment.
     * @param lab The probability for lab
     * @param xray The probability for x-ray
     * @param treatment The probability for treatment
     */
    @Override
    public void setProbabilities(double lab, double xray, double treatment, double noTreatment) {
        probabilityDao.update(new Probability("LAB", lab));
        probabilityDao.update(new Probability("XRAY", xray));
        probabilityDao.update(new Probability("TREATMENT", treatment));
        probabilityDao.update(new Probability("NO_TREATMENT", noTreatment));
    }

    /**
     * Updates the distribution for a given event.
     * @param event The event name
     * @param distribution The distribution type
     * @param averageTime The average time for the event
     */
    @Override
    public void updateDistribution(String event, String distribution, double averageTime) {
        distributionDao.update(new Distribution(event, distribution, averageTime));
    }

    /**
     * Retrieves the probability for a given decision type.
     * @param decisionType The decision type
     * @return The probability
     */
    @Override
    public double getProbability(String decisionType) {
        return probabilityDao.find(decisionType).getProbability();
    }

    /**
     * Retrieves the average time for a given event.
     * @param eventName The event name
     * @return The average time
     */
    @Override
    public double getAverageTime(String eventName) {
        return distributionDao.find(eventName).getAverageTime();
    }

    /**
     * Retrieves the distribution type for a given event.
     * @param event The event name
     * @return The distribution type
     */
    @Override
    public String getDistribution(String event) {
        return distributionDao.find(event).getDistribution();
    }

    /**
     * Retrieves the distribution object for a given event.
     * @param event The event name
     * @return The distribution object
     */
    @Override
    public Distribution getDistributionObject(String event) {
        return distributionDao.find(event);
    }

    /**
     * Sets default distributions for events.
     */
    @Override
    public void setDefaultDistributions() {
        distributionDao.update(new Distribution("arrival", "negexp", 15));
        distributionDao.update(new Distribution("check-in", "negexp", 3));
        distributionDao.update(new Distribution("doctor", "negexp", 5));
        distributionDao.update(new Distribution("lab", "negexp", 10));
        distributionDao.update(new Distribution("xray", "negexp", 8));
        distributionDao.update(new Distribution("treatment", "negexp", 12));
    }

    /**
     * Retrieves all simulation results.
     * @return The list of simulation results
     */
    @Override
    public List<SimulationResults> getSimulationResults() {
        return simulationResultsDao.findAll();
    }

    /**
     * Persists the given simulation results.
     * @param simulationResults The simulation results to persist
     */
    @Override
    public void persistSimulationResults(SimulationResults simulationResults) {
        simulationResultsDao.persist(simulationResults);
    }
}
