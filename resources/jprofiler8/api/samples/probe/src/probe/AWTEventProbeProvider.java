package probe;

import com.jprofiler.api.agent.probe.Probe;
import com.jprofiler.api.agent.probe.ProbeProvider;

// The probe provider class is specified with as a VM parameter as
// -Djprofiler.probeProvider=probe.AWTEventProbeProvider
// so the JProfiler agent can initialize all interceptors at startup.
// The interceptor class and all referenced classes have to be in the boot classpath
// so they can be accessed by the profiling agent. 
public class AWTEventProbeProvider implements ProbeProvider {

    @Override
    public Probe[] getProbes() {
        return new Probe[] {
            new AWTEventProbe()
        };
    }
}
