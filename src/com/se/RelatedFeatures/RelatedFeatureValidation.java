/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se.RelatedFeatures;

import com.se.Exceptions.*;

/**
 *
 * @author Hanan_Sabry
 */
public interface RelatedFeatureValidation {

    public void validatePlName() throws PlException;

    public void validateVendorName() throws VendorException;

    public void validateIdFeatureName() throws FeatureNameException, PlException;

    public void validateUpdatedFeatureName() throws FeatureNameException;
    
    public void validateIdFeatureValue() throws NotValidRelatedFeatureException;
    
    public void validateUpdatedFeatureValue() throws FeatureValueException, ApprovedValueException;
}
