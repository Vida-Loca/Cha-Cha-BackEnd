package com.vidaloca.skibidi.friendship.exception;

public class RelationNotFoundException extends RuntimeException{
    public RelationNotFoundException(Long userId, Long userId2){
        super("Relation between users with ids: "+ userId+" and " +userId2 + " not exists") ;
    }
}
