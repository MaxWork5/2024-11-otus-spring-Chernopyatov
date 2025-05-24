package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {
    private final MutableAclService aclService;

    @Override
    public void createPermission(Object object, List<Permission> permissions) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var owner = new PrincipalSid(authentication);
        var admin = new GrantedAuthoritySid("ROLE_ADMIN");

        var acl = aclService.createAcl(new ObjectIdentityImpl(object));
        for (Permission permission : permissions) {
            acl.insertAce(acl.getEntries().size(), permission, owner, true);
            acl.insertAce(acl.getEntries().size(), permission, admin, true);
        }
        aclService.updateAcl(acl);
    }

    @PostFilter("canRead(filterObject.id, T(ru.otus.hw.domain.Book))")
    @Override
    public List<Book> filterBooks(List<Book> books) {
        return books;
    }
}