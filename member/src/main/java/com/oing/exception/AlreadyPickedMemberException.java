package com.oing.exception;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/2/24
 * Time: 12:03 PM
 */
public class AlreadyPickedMemberException extends DomainException{
    public AlreadyPickedMemberException() {
        super(ErrorCode.ALREADY_PICKED_TODAY);
    }
}
