package br.com.oiinternet.oiapi.domain;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import br.com.oiinternet.oiapi.sdk.enumeration.Status;
import br.com.oiinternet.oiapi.sdk.enumeration.SystemSource;

/**
 * Minha Oi associtation with Oi Internet account CPF + POID ACCOUNT
 */
public class MoiAssociation implements Serializable {

    private static final long serialVersionUID = 2017041202L;

    @Id
    private ObjectId id;

    private String cpfCnpj;

    private String accountPoid;

    private String name;

    private String lastName;

    private String login;

    private String email;

    private boolean physical;

    private Date associationDate;

    private Status status;

    private Date modifiedDate;

    private SystemSource systemSource;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public SystemSource getSystemSource() {
        return systemSource;
    }

    public void setSystemSource(SystemSource systemSource) {
        this.systemSource = systemSource;
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
        result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (physical ? 1231 : 1237);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((systemSource == null) ? 0 : systemSource.hashCode());
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
        if (modifiedDate == null) {
            if (other.modifiedDate != null) {
                return false;
            }
        } else if (!modifiedDate.equals(other.modifiedDate)) {
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
        if (status != other.status) {
            return false;
        }
        if (systemSource != other.systemSource) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MoiAssociation [");
        if (id != null) {
            builder.append("id: ");
            builder.append(id);
            builder.append(" | ");
        }
        if (cpfCnpj != null) {
            builder.append("cpfCnpj: ");
            builder.append(cpfCnpj);
            builder.append(" | ");
        }
        if (accountPoid != null) {
            builder.append("accountPoid: ");
            builder.append(accountPoid);
            builder.append(" | ");
        }
        if (name != null) {
            builder.append("name: ");
            builder.append(name);
            builder.append(" | ");
        }
        if (lastName != null) {
            builder.append("lastName: ");
            builder.append(lastName);
            builder.append(" | ");
        }
        if (login != null) {
            builder.append("login: ");
            builder.append(login);
            builder.append(" | ");
        }
        if (email != null) {
            builder.append("email: ");
            builder.append(email);
            builder.append(" | ");
        }
        builder.append("physical: ");
        builder.append(physical);
        builder.append(" | ");
        if (associationDate != null) {
            builder.append("associationDate: ");
            builder.append(associationDate);
            builder.append(" | ");
        }
        if (status != null) {
            builder.append("status: ");
            builder.append(status);
            builder.append(" | ");
        }
        if (modifiedDate != null) {
            builder.append("modifiedDate: ");
            builder.append(modifiedDate);
            builder.append(" | ");
        }
        if (systemSource != null) {
            builder.append("systemSource: ");
            builder.append(systemSource);
        }
        builder.append("]");
        return builder.toString();
    }

}
