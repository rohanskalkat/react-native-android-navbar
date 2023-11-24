
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNAndroidNavbarSpec.h"

@interface AndroidNavbar : NSObject <NativeAndroidNavbarSpec>
#else
#import <React/RCTBridgeModule.h>

@interface AndroidNavbar : NSObject <RCTBridgeModule>
#endif

@end
