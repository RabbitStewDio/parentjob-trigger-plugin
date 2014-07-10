package org.jenkinsci.plugins.sample;

import antlr.ANTLRException;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.triggers.SCMTrigger;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import hudson.util.SequentialExecutionQueue;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class ParentProjectTrigger extends SCMTrigger {

  private boolean onlyDirectReferences;

  @DataBoundConstructor
  public ParentProjectTrigger(final String cronTabSpec, final boolean onlyDirectReferences) throws ANTLRException {
    super(cronTabSpec);
    this.onlyDirectReferences = onlyDirectReferences;
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

  public void setOnlyDirectReferences(final boolean onlyDirectReferences) {
    this.onlyDirectReferences = onlyDirectReferences;
  }

  @Extension
  @SuppressWarnings("unused")
  public static class ParentProjectTriggerDescriptor extends DescriptorImpl {

    private final transient SequentialExecutionQueue queue = new SequentialExecutionQueue(Executors.newSingleThreadExecutor());

    @Override
    public String getDisplayName() {
      return "[ParentProjectTrigger] - Monitor this and all parent jobs for SCM changes";
    }

    @Override
    public String getHelpFile() {
      return null;
    }
  }

}
