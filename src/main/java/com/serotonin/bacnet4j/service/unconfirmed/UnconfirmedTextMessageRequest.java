/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Infinite Automation Software,
 * the following extension to GPL is made. A special exception to the GPL is
 * included to allow you to distribute a combined work that includes BAcnet4J
 * without being obliged to provide the source code for any proprietary components.
 *
 * See www.infiniteautomation.com for commercial license options.
 * 
 * @author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.service.unconfirmed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.NPCI.NetworkPriority;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class UnconfirmedTextMessageRequest extends UnconfirmedRequestService {
    private static final long serialVersionUID = 1555323189158421374L;

    public static final byte TYPE_ID = 5;

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(UnsignedInteger.class);
        classes.add(CharacterString.class);
    }
    private final ObjectIdentifier textMessageSourceDevice;
    private Choice messageClass;
    private final MessagePriority messagePriority;
    private final CharacterString message;

    public UnconfirmedTextMessageRequest(ObjectIdentifier textMessageSourceDevice, UnsignedInteger messageClass,
            MessagePriority messagePriority, CharacterString message) {
        this.textMessageSourceDevice = textMessageSourceDevice;
        this.messageClass = new Choice(0, messageClass);
        this.messagePriority = messagePriority;
        this.message = message;
    }

    public UnconfirmedTextMessageRequest(ObjectIdentifier textMessageSourceDevice, CharacterString messageClass,
            MessagePriority messagePriority, CharacterString message) {
        this.textMessageSourceDevice = textMessageSourceDevice;
        this.messageClass = new Choice(0, messageClass);
        this.messagePriority = messagePriority;
        this.message = message;
    }

    public UnconfirmedTextMessageRequest(ObjectIdentifier textMessageSourceDevice, MessagePriority messagePriority,
            CharacterString message) {
        this.textMessageSourceDevice = textMessageSourceDevice;
        this.messagePriority = messagePriority;
        this.message = message;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public NetworkPriority getNetworkPriority() {
        return messagePriority.getNetworkPriority();
    }

    @Override
    public void handle(LocalDevice localDevice, Address from) {
        localDevice.getEventHandler().fireTextMessage(
                localDevice.getRemoteDeviceCreate(textMessageSourceDevice.getInstanceNumber(), from), messageClass,
                messagePriority, message);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, textMessageSourceDevice, 0);
        writeOptional(queue, messageClass, 1);
        write(queue, messagePriority, 2);
        write(queue, message, 3);
    }

    UnconfirmedTextMessageRequest(ByteQueue queue) throws BACnetException {
        textMessageSourceDevice = read(queue, ObjectIdentifier.class, 0);
        if (readStart(queue) == 1)
            messageClass = new Choice(queue, classes);
        messagePriority = read(queue, MessagePriority.class, 2);
        message = read(queue, CharacterString.class, 3);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((message == null) ? 0 : message.hashCode());
        result = PRIME * result + ((messageClass == null) ? 0 : messageClass.hashCode());
        result = PRIME * result + ((messagePriority == null) ? 0 : messagePriority.hashCode());
        result = PRIME * result + ((textMessageSourceDevice == null) ? 0 : textMessageSourceDevice.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UnconfirmedTextMessageRequest other = (UnconfirmedTextMessageRequest) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        }
        else if (!message.equals(other.message))
            return false;
        if (messageClass == null) {
            if (other.messageClass != null)
                return false;
        }
        else if (!messageClass.equals(other.messageClass))
            return false;
        if (messagePriority == null) {
            if (other.messagePriority != null)
                return false;
        }
        else if (!messagePriority.equals(other.messagePriority))
            return false;
        if (textMessageSourceDevice == null) {
            if (other.textMessageSourceDevice != null)
                return false;
        }
        else if (!textMessageSourceDevice.equals(other.textMessageSourceDevice))
            return false;
        return true;
    }
}
