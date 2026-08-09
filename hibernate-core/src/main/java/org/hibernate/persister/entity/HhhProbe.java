/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.persister.entity;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.sql.ast.tree.from.TableGroup;

public final class HhhProbe {

	public static String shorten(String name) {
		return name == null ? "null" : name.replaceAll( "[A-Za-z0-9_.]*\\$", "" );
	}

	private static List<String> joins(TableGroup tableGroup) {
		return tableGroup.getTableReferenceJoins().stream()
				.map( j -> j.getJoinedTableReference().getTableId() ).sorted().toList();
	}

	public static void foreignKeyJoin(
			String role, String sideNature, String fkTargetTable,
			String associatedEntityTable, boolean duringQueryTranslation) {
		if ( duringQueryTranslation ) {
			System.out.println( "  [foreign-key-join] " + shorten( role )
					+ "\n      sideNature              = " + sideNature
					+ "\n      foreignKeyTargetTable   = " + fkTargetTable
					+ "\n      associatedEntityTable   = " + associatedEntityTable );
		}
	}

	public static void pruneEnter(String entityName, TableGroup tableGroup, Map<String, EntityNameUse> uses) {
		final var rendered = new TreeSet<String>();
		for ( var e : uses.entrySet() ) {
			rendered.add( shorten( e.getKey() ) + "=" + e.getValue().getKind() );
		}
		System.out.println( "  [prune] " + shorten( entityName )
				+ "  path=" + shorten( String.valueOf( tableGroup.getNavigablePath() ) )
				+ "\n      registeredEntityNameUses = " + rendered
				+ "\n      tableJoinsBeforePruning  = " + joins( tableGroup ) );
	}

	public static void pruneExit(TableGroup tableGroup) {
		System.out.println( "      tableJoinsAfterPruning   = " + joins( tableGroup ) );
	}
}
