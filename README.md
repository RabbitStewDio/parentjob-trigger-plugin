Parent-Job-Trigger Plugin for Jenkins
-------------------------------------

When working with chains of Jenkins build configurations,
it can be difficult to enforce a correct build sequence.
Jenkins jobs trigger independently of each other and 
although Jenkins can block a build when a parent job is
scheduled, this can still cause broken builds if the
parent job is not polled before the child job starts
building.

This simple plugin allows you to poll all SCM repositories
for changes. If a change is found, the plugin will trigger
a poll of all parent-projects. If these projects have 
changes, they will now be scheduled before this build
starts and thus the projects will be built in the correct
order.


Usage
-----
Build the plugin via "mvn verify" and upload the resulting
HPI file to Jenkins and activate the plugin. The plugin
will appear in all projects in the build=triggers section.

This trigger replaces the normal "Poll SCM" trigger. Therefore
make sure that no other build trigger is active or the
build order will not be enforced correctly.
