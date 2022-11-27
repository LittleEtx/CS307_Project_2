package cs307.project2.interfaces;

import java.io.Serializable;

/*
 * <p>
 * Full information of the containers
 * <p>
 * @classname: ContainerInfo
 * @param shipping: whether this container is being shipped or not
 */
public record ContainerInfo(Type type, String code, boolean using) implements Serializable {
	public enum Type {
		Dry, FlatRack, ISOTank, OpenTop, Reefer
	}
}
