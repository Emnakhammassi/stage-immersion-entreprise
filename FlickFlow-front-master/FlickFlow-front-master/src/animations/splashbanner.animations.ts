import {animate, animation, state, style, transition, trigger} from "@angular/animations";

export const splashbannerAnimations = animation([
  trigger('backdropToVideo', [
    state('backdrop', style({
      opacity: 1
    })),
    state('video', style({
      opacity: 1
    })),
    transition('backdrop => video', [
      animate('1s', style({ opacity: 0 }))
    ])
  ])
])
