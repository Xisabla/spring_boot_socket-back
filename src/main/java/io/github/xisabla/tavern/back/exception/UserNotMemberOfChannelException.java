package io.github.xisabla.tavern.back.exception;

import io.github.xisabla.tavern.back.model.Channel;
import io.github.xisabla.tavern.back.model.User;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not a member of a channel and tries to perform an action that requires membership.
 */
public class UserNotMemberOfChannelException extends APIException {
    /**
     * @param user    User that is not a member of the channel.
     * @param channel Channel the user is not a member of.
     */
    public UserNotMemberOfChannelException(final User user, final Channel channel) {
        super(
            HttpStatus.FORBIDDEN,
            "User " + user.getUsername() + " (" + user.getId() + ") is not a member of the channel " + channel.getName()
                + " (" + channel.getId() + ")");
    }

    /**
     * @param channel Channel the user is not a member of.
     */
    public UserNotMemberOfChannelException(final Channel channel) {
        super(
            HttpStatus.FORBIDDEN,
            "User is not a member of the channel " + channel.getName() + " (" + channel.getId() + ")");
    }
}
