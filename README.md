# CSC-540-Project

Course Project for CSC 540 - Database Management Concepts and Systems

For this group project, students were tasked with designing and implementing
a database system for a publishing company. The system was designed for publishing
employees fulfilling multiple roles and responsibilities. Users of the system can
create and edit publications (books, journals, magazines) and their contents. Customer
orders can be entered, billed, and tracked. Publishing staff assignments can be managed
and payroll is issued and tracked.

Throughout the project, the group created entity-relationship diagrams for the
database and determined the relational database schema needed for the application.
APIs were written to interface with the database via JDBC. Transactions were utilized
to improve data reliability.

The application UI was designed as command line menu input for all user interaction.
Access to a MariaDB server was provided for the course, but is no longer accessible.

However, for demonstration purposes, a containerized version of the app bundled as a Docker compose project with a MariaDB server has been added in the `demo` directory.