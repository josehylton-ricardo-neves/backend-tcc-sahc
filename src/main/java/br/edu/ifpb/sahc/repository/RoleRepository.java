package br.edu.ifpb.sahc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.sahc.model.Role;


public interface RoleRepository extends JpaRepository<Role, UUID> {

}
