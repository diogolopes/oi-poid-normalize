package br.com.oiinternet.poidnormalizer.domain;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.oiinternet.oiapi.sdk.enumeration.SystemSource;

@Document(collection = "moiAssociation")
public class MoiAssociation implements Serializable {

    private static final long serialVersionUID = 2017041201L;

    private ObjectId id;

    private String cpfCnpj;

    private String accountPoid;

    private String name;

    private String lastName;

    private String login;

    private String email;

    private boolean physical;

    private Date associationDate;

    private Date modifiedDate;

    private SystemSource systemSource;

    public SystemSource getSystemSource() {
        return systemSource;
    }

    public void setSystemSource(SystemSource systemSource) {
        this.systemSource = systemSource;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getAccountPoid() {
        return accountPoid;
    }

    public void setAccountPoid(String accountPoid) {
        this.accountPoid = accountPoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPhysical() {
        return physical;
    }

    public void setPhysical(boolean physical) {
        this.physical = physical;
    }

    public Date getAssociationDate() {
        return associationDate;
    }

    public void setAssociationDate(Date associationDate) {
        this.associationDate = associationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountPoid == null) ? 0 : accountPoid.hashCode());
        result = prime * result + ((associationDate == null) ? 0 : associationDate.hashCode());
        result = prime * result + ((cpfCnpj == null) ? 0 : cpfCnpj.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (physical ? 1231 : 1237);
        result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MoiAssociation)) {
            return false;
        }
        MoiAssociation other = (MoiAssociation) obj;
        if (accountPoid == null) {
            if (other.accountPoid != null) {
                return false;
            }
        } else if (!accountPoid.equals(other.accountPoid)) {
            return false;
        }
        if (associationDate == null) {
            if (other.associationDate != null) {
                return false;
            }
        } else if (!associationDate.equals(other.associationDate)) {
            return false;
        }
        if (cpfCnpj == null) {
            if (other.cpfCnpj != null) {
                return false;
            }
        } else if (!cpfCnpj.equals(other.cpfCnpj)) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        if (login == null) {
            if (other.login != null) {
                return false;
            }
        } else if (!login.equals(other.login)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (physical != other.physical) {
            return false;
        }
        if (modifiedDate == null) {
            if (other.modifiedDate != null) {
                return false;
            }
        } else if (!modifiedDate.equals(other.modifiedDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MoiAssociation [" + (id != null ? "id: " + id + " | " : "")
                + (cpfCnpj != null ? "cpfCnpj: " + cpfCnpj + " | " : "")
                + (accountPoid != null ? "accountPoid: " + accountPoid + " | " : "")
                + (name != null ? "name: " + name + " | " : "")
                + (systemSource != null ? "systemSource: " + systemSource + " | " : "")
                + (lastName != null ? "lastName: " + lastName + " | " : "")
                + (login != null ? "login: " + login + " | " : "") + (email != null ? "email: " + email + " | " : "")
                + "physical: " + physical + " | "
                + (associationDate != null ? "associationDate: " + associationDate + " | " : "")
                + (modifiedDate != null ? "modifiedDate: " + modifiedDate : "") + "]";
    }

}
