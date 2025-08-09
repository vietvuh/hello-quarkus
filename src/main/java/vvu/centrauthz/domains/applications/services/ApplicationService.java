package vvu.centrauthz.domains.applications.services;

import jakarta.inject.Singleton;
import vvu.centrauthz.domains.applications.models.Application;
import vvu.centrauthz.domains.applications.models.ApplicationFilter;
import vvu.centrauthz.domains.applications.models.ApplicationForPatch;
import vvu.centrauthz.domains.applications.models.ApplicationPatcher;
import vvu.centrauthz.errors.NotImplementedError;
import vvu.centrauthz.models.Page;
import vvu.centrauthz.models.Patcher;
import vvu.centrauthz.utilities.Context;

@Singleton
public class ApplicationService {
    public Page<Application, String> list(ApplicationFilter filter, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

    public Application get(String applicationKey, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

    public Application create(Application application, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

    public void update(String applicationKey, Application application, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

    public void update(String applicationKey, Patcher<ApplicationPatcher> applicationForPatch, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

    public void delete(String applicationKey, Context context) {
        throw new NotImplementedError("Feature not implemented");
    }

}
