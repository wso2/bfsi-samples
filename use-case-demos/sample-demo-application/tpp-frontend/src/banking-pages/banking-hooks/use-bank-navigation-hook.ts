/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import type { Step, Type, UseCase} from "../../hooks/config-interfaces.ts";
import {useCallback, useEffect, useRef, useState} from "react";

interface BankingHookProps {
    usecase: Type[];
    type: string;
}

/**
 * @function useBankNavigationHook
 * @description A custom hook that manages the state and flow of multi-step banking authorization
 * processes (use cases). It determines the initial route type, manages the sequence of
 * steps, tracks the current step index, and provides a handler to advance the process upon success.
 */
export const useBankNavigationHook = ({usecase,type,}:BankingHookProps)=>{

    const initialTypeIndex = usecase.findIndex(item => item.id === type);
    const mainUseecaseAction = initialTypeIndex !== -1 ? initialTypeIndex : 0;
    const routeType =  useRef(mainUseecaseAction)
    const initialUsecaseItem = usecase[routeType.current];
    const initialSteps = initialUsecaseItem?.useCases[0]?.steps || [];
    const [sequence, setSequence] = useState<Step[]>(initialSteps);
    const [steps, setSteps] = useState<number>(0);
    const [currentStep, setCurrentStep] = useState<Step >(sequence[0]);
    const [usecasesList, setUseCasesList] = useState<UseCase[]>(initialUsecaseItem?.useCases || []);
    const [selectedUsecaseIndex, setSelectedUsecaseIndex] = useState<number>(0);

    const usecaseSelectionHandler = useCallback(
        (indexOfUsecase: number = 0) => {
            const newSteps = usecase[routeType.current]?.useCases[indexOfUsecase]?.steps;
            if (newSteps) {
                setSelectedUsecaseIndex(indexOfUsecase);
                setSequence(newSteps);
                setSteps(0);
                setCurrentStep(newSteps[0]);
            }
        },
        [usecase, routeType.current]
    );

    useEffect(() => {
        const list = usecase[routeType.current]?.useCases || [];
        setUseCasesList(list);
    }, [selectedUsecaseIndex,sequence]);

    const onSuccessHandler =()=>{
        if (steps < sequence.length-1) {
            setSteps((steps)=> steps+1)
        }
        const step = sequence[steps+1];
        setCurrentStep(step);
    }
    return {usecasesList, onSuccessHandler,currentStep,selectedUsecaseIndex,usecaseSelectionHandler}
}
