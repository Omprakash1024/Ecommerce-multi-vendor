package com.app.oneplace.domain;

public enum AccountStatus {

	PENDING_VERIFICATION, // Account is created but not yet verified
	ACTIVE,               // Account is active and in good standing
	SUSPENDED,            // Account is temporarily suspended, possibility due to violation
	DEACTIVATED,          // Account is deactivated, user might have chosen to deactivate it
	BANNED,               // Account is permanently banned due to severe violation
	CLOSED                // Account is permanently closed, possibly at user request
}
