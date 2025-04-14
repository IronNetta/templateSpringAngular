import {AbstractControl, FormGroup, ValidatorFn} from '@angular/forms';

export const samePasswordValidator = (): ValidatorFn | null => {
  return (group: AbstractControl) => {
    let password = group.get('password')?.value;
    let confirmPassword = group.get('confirmPassword')?.value;
    if(!password || !confirmPassword) {
      return null;
    }
    if(password === confirmPassword) {
      return null;
    }
    return {confirmPassword: true};
  }
}
