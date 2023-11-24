import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  changeNavigationBarColor(
    color: string,
    light: boolean,
    animated: boolean
  ): Promise<boolean>;
  hideNavigationBar(): Promise<boolean>;
  showNavigationBar(): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('AndroidNavbar');
