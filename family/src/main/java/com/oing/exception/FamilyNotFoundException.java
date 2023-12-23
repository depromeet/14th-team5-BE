package com.oing.exception;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/23/23
 * Time: 7:38 PM
 */
public class FamilyNotFoundException extends DomainException {
    public FamilyNotFoundException() {
        super(ErrorCode.FAMILY_NOT_FOUND);
    }
}
