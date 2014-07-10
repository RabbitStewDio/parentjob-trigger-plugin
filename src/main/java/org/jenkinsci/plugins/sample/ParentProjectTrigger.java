package org.jenkinsci.plugins.sample;

import antlr.ANTLRException;
import hudson.model.AbstractProject;
import hudson.triggers.SCMTrigger;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ParentProjectTrigger extends SCMTrigger {

  private boolean onlyDirectReferences;


  public ParentProjectTrigger(final String cronTabSpec) throws ANTLRException {
    super(cronTabSpec);
  }

  @Override
  public void run() {
    final AbstractProject<?, ?> abstractProject = job.asProject();
    final List<AbstractProject> upstreamProjects = onlyDirectReferences ?
            abstractProject.getUpstreamProjects() : abstractProject.getBuildTriggerUpstreamProjects();
    for (final AbstractProject upstreamProject : upstreamProjects) {
      final Map<TriggerDescriptor, Trigger<?>> triggers = upstreamProject.getTriggers();
      final Collection<Trigger<?>> values = triggers.values();
      for (final Trigger<?> trigger : values) {
        trigger.run();
      }
    }

    super.run();
  }

  public boolean isOnlyDirectReferences() {
    return onlyDirectReferences;
  }

  public void setOnlyDirectReferences(boolean onlyDirectReferences) {
    this.onlyDirectReferences = onlyDirectReferences;
  }
}
