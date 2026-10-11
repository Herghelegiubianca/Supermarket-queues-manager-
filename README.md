Multi-Threaded Queue Management Simulation

A Java-based desktop application developed to simulate and analyze waiting queues
(e.g., store checkout counters). The system leverages advanced multithreading concepts
to run each server (checkout counter) on a separate thread, dynamically allocating tasks
(clients) based on configurable strategies, all visualized in real-time through a graphical
user interface.

Features:

Concurrent Simulation: Each server runs independently on its own thread, processing tasks
from a thread-safe data structure (BlockingQueue).

Dynamic Routing (Strategy Pattern): Clients are automatically distributed to servers using
interchangeable runtime policies: ShortestQueueStrategy (fewest clients) or ShortestTimeStrategy
(lowest estimated waiting time).

Monitoring & Statistics: Calculates and outputs detailed analytical data at the end of the simulation,
including average waiting time, average service time, and peak hour.

Real-Time Animated UI: A dedicated graphical interface (SimulationFrame) that dynamically draws and 
updates the waiting lines as the simulation progresses.
